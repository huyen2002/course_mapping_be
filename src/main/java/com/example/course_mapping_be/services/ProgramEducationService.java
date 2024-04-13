package com.example.course_mapping_be.services;

import com.example.course_mapping_be.constraints.LevelEducationType;
import com.example.course_mapping_be.constraints.RoleType;
import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.Major;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.repositories.MajorRepository;
import com.example.course_mapping_be.repositories.ProgramEducationRepository;
import com.example.course_mapping_be.repositories.UniversityRepository;
import com.example.course_mapping_be.repositories.UserRepository;
import com.example.course_mapping_be.security.JsonWebTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor

public class ProgramEducationService {

    private JsonWebTokenProvider tokenProvider;

    private UniversityRepository universityRepository;
    private MajorRepository majorRepository;
    private ProgramEducationRepository programEducationRepository;
    private ModelMapper modelMapper;
    private UserRepository userRepository;
    private ReadFileService readFileService;
    private DocumentService documentService;

    public BaseResponse<ProgramEducationDto> create(ProgramEducationDto programEducationDto, HttpServletRequest request) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User is not found"));
        University university;
        if (user.getRole() == RoleType.UNIVERSITY) {
            university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));

        } else if (user.getRole() == RoleType.ADMIN) {
            university = universityRepository.findById(programEducationDto.getUniversityId()).orElseThrow(() -> new Exception("University is not found"));
        } else {
            throw new Exception("You are not authorized to create program education");

        }

        Major major = majorRepository.findById(programEducationDto.getMajorId()).orElseThrow(() -> new Exception("Major is not found"));
        ProgramEducation programEducation = ProgramEducation.builder().name(programEducationDto.getName())
                .language(programEducationDto.getLanguage())
                .levelOfEducation(LevelEducationType.valueOf(programEducationDto.getLevelOfEducation()))
                .university(university)
                .code(programEducationDto.getCode())
                .major(major).introduction(programEducationDto.getIntroduction())
                .durationYear(programEducationDto.getDurationYear())
                .numCredits(programEducationDto.getNumCredits())
                .outline(programEducationDto.getOutline())
                .startYear(programEducationDto.getStartYear())
                .endYear(programEducationDto.getEndYear())
                .sourceLinks(programEducationDto.getSourceLinks())
                .build();


        programEducationRepository.save(programEducation);
        BaseResponse<ProgramEducationDto> baseResponse = new BaseResponse<>();

        baseResponse.setData((modelMapper.map(programEducation, ProgramEducationDto.class)));
        baseResponse.success();
        return baseResponse;

    }

    public BaseResponse<List<ProgramEducationDto>> getAllByUniversityId(Long id, QueryParams params) throws Exception {
        BaseResponse<List<ProgramEducationDto>> baseResponse = new BaseResponse<>();
        if (universityRepository.findById(id).isEmpty()) {
            throw new Exception("University is not found");
        }
        Page<ProgramEducation> programEducations = programEducationRepository.findAllByUniversityId(id, PageRequest.of(params.getPage(), params.getSize()));
        List<ProgramEducationDto> programEducationDtos = programEducations.map(programEducation -> modelMapper.map(programEducation, ProgramEducationDto.class)).getContent();
        baseResponse.setData(programEducationDtos);
        baseResponse.success();
        baseResponse.updatePagination(params, programEducations.getTotalElements());
        return baseResponse;
    }

    public BaseResponse<List<ProgramEducationDto>> getAllByMajorId(Long id, QueryParams params) throws Exception {
        BaseResponse<List<ProgramEducationDto>> baseResponse = new BaseResponse<>();
        if (majorRepository.findById(id).isEmpty()) {
            throw new Exception("Major with id is not found");
        }
        Page<ProgramEducation> programEducations = programEducationRepository.findAllByMajorId(id, PageRequest.of(params.getPage(), params.getSize()));
        List<ProgramEducationDto> programEducationDtos = programEducations.map(programEducation -> modelMapper.map(programEducation, ProgramEducationDto.class)).getContent();
        baseResponse.setData(programEducationDtos);
        baseResponse.success();
        baseResponse.updatePagination(params, programEducations.getTotalElements());
        return baseResponse;
    }

    public BaseResponse<ProgramEducationDto> update(Long id, ProgramEducationDto programEducationDto, HttpServletRequest request) throws Exception {
        ProgramEducation programEducation = programEducationRepository.findById(id).orElseThrow(() -> new Exception("Program education is not found"));
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));
        if (!programEducation.getUniversity().getId().equals(university.getId())) {
            throw new Exception("You are not authorized to update this program education");
        }
        if (programEducationDto.getName() != null) {
            programEducation.setName(programEducationDto.getName());
        }
        if (programEducationDto.getLanguage() != null) {
            programEducation.setLanguage(programEducationDto.getLanguage());
        }
        if (programEducationDto.getIntroduction() != null) {
            programEducation.setIntroduction(programEducationDto.getIntroduction());
        }
        if (programEducationDto.getDurationYear() != null) {
            programEducation.setDurationYear(programEducationDto.getDurationYear());
        }
        if (programEducationDto.getLevelOfEducation() != null) {
            programEducation.setLevelOfEducation(LevelEducationType.valueOf(programEducationDto.getLevelOfEducation()));
        }
        if (programEducationDto.getNumCredits() != null) {
            programEducation.setNumCredits(programEducationDto.getNumCredits());
        }
        if (programEducationDto.getOutline() != null) {
            programEducation.setOutline(programEducationDto.getOutline());
        }
        if (programEducationDto.getStartYear() != null) {
            programEducation.setStartYear(programEducationDto.getStartYear());
        }
        if (programEducationDto.getEndYear() != null) {
            programEducation.setEndYear(programEducationDto.getEndYear());
        }
        if (programEducationDto.getMajorId() != null) {
            Major major = majorRepository.findById(programEducationDto.getMajorId()).orElseThrow(() -> new Exception("Major is not found"));
            programEducation.setMajor(major);
        }
        if (programEducationDto.getSourceLinks() != null) {
            programEducation.setSourceLinks(programEducationDto.getSourceLinks());
        }
        programEducationRepository.save(programEducation);
        BaseResponse<ProgramEducationDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(programEducation, ProgramEducationDto.class));
        baseResponse.success();
        return baseResponse;

    }

    public BaseResponse<ProgramEducationDto> getById(Long id) throws Exception {
        ProgramEducation programEducation = programEducationRepository.findById(id).orElseThrow(() -> new Exception("Program education is not found"));
        BaseResponse<ProgramEducationDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(programEducation, ProgramEducationDto.class));
        baseResponse.success();
        return baseResponse;
    }

    public Boolean deleteById(Long id, HttpServletRequest request) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));
        ProgramEducation programEducation = programEducationRepository.findById(id).orElseThrow(() -> new Exception("Program education is not found"));
        if (!programEducation.getUniversity().getId().equals(university.getId())) {
            throw new Exception("You are not authorized to delete this program education");
        }
        programEducationRepository.deleteById(id);
        return true;

    }

    public BaseResponse<List<ProgramEducationDto>> getAll(QueryParams params) {
        BaseResponse<List<ProgramEducationDto>> baseResponse = new BaseResponse<>();

        Page<ProgramEducation> programEducations = programEducationRepository.findAll(PageRequest.of(params.getPage(), params.getSize()));
        List<ProgramEducationDto> programEducationDtos = programEducations.map(programEducation -> modelMapper.map(programEducation, ProgramEducationDto.class)).getContent();
        baseResponse.setData(programEducationDtos);
        baseResponse.success();
        baseResponse.updatePagination(params, programEducations.getTotalElements());
        return baseResponse;
    }

    public BaseResponse<List<ProgramEducationDto>> search(SearchProgramDto searchProgramDto, QueryParams params) {
        BaseResponse<List<ProgramEducationDto>> baseResponse = new BaseResponse<>();

        Page<ProgramEducation> programEducations = programEducationRepository.searchPrograms(searchProgramDto, PageRequest.of(params.getPage(), params.getSize()));
        List<ProgramEducationDto> programEducationDtos = programEducations.map(programEducation -> modelMapper.map(programEducation, ProgramEducationDto.class)).getContent();
        baseResponse.setData(programEducationDtos);
        baseResponse.success();
        baseResponse.updatePagination(params, programEducations.getTotalElements());
        return baseResponse;
    }

    public BaseResponse<Float> compareTwoPrograms(Long id1, Long id2) throws Exception {
        BaseResponse<Float> baseResponse = new BaseResponse<>();

        ProgramEducation programEducation1 = programEducationRepository.findById(id1).orElseThrow(() -> new Exception("Program education is not found"));
        ProgramEducation programEducation2 = programEducationRepository.findById(id2).orElseThrow(() -> new Exception("Program education is not found"));
        if (programEducation1.getVectorDocument() == null || programEducation2.getVectorDocument() == null) {
            baseResponse.setData((float) 0);
            baseResponse.success();
            return baseResponse;
        }
        JSONObject jsonObject1 = new JSONObject(programEducation1.getVectorDocument());
        JSONObject jsonObject2 = new JSONObject(programEducation2.getVectorDocument());
       
        List<Object> vector1 = jsonObject1.getJSONArray("vector").toList();
        List<Object> vector2 = jsonObject2.getJSONArray("vector").toList();
        String uri = "http://localhost:5000/api/cossim_between_two_vectors";
        RestTemplate restTemplate = new RestTemplate();
        CompareTwoVectorsDto compareTwoVectorsDto = new CompareTwoVectorsDto(vector1, vector2);
        Float result = restTemplate.postForObject(uri, compareTwoVectorsDto, Float.class);
        baseResponse.setData(result);
        baseResponse.success();
        return baseResponse;

    }

    public BaseResponse<List<Pair<ProgramEducationDto, Float>>> getTopSimilar(Long id) throws Exception {
        ProgramEducation programEducation = programEducationRepository.findById(id).orElseThrow(() -> new Exception("Program education is not found"));
        List<ProgramEducation> programEducations = programEducationRepository.findAll();
        List<Pair<ProgramEducationDto, Float>> programEducationDtos = new ArrayList<>();
        // similarity between program education and other program educations and sort by similarity from high to low
        for (ProgramEducation program : programEducations) {
            if (!Objects.equals(program.getId(), id)) {
                Float similarity = compareTwoPrograms(id, program.getId()).getData();
                programEducationDtos.add(Pair.of(modelMapper.map(program, ProgramEducationDto.class), similarity));
            }

        }
        // sort programEducationDtos by similarity from high to low
        programEducationDtos.sort((o1, o2) -> {
            if (o1.getSecond() > o2.getSecond()) return -1;
            if (o1.getSecond() < o2.getSecond()) return 1;
            return 0;
        });
        BaseResponse<List<Pair<ProgramEducationDto, Float>>> baseResponse = new BaseResponse<>();
        // return top 10 most similar program educations
        baseResponse.setData(programEducationDtos.subList(0, 10));
        baseResponse.success();
        return baseResponse;


//        final String uri = "http://127.0.0.1:5000/api/top_n_most_similar";
//
//        RestTemplate restTemplate = new RestTemplate();
//        ProgramOutlineDto programOutlineDto = new ProgramOutlineDto(programEducation.getIntroduction());
//
//        String result = restTemplate.postForObject(uri, programOutlineDto, String.class);
//        JSONObject jsonObject = new JSONObject(result);
//        List<String> files = new ArrayList<>();
//        try {
//            JSONArray fileList = (JSONArray) jsonObject.get("list");
//            for (Object item : fileList) {
//                JSONArray innerArray = (JSONArray) item;
//                System.out.println(innerArray);
//                String fileName = (String) innerArray.get(0);
//
//                files.add(fileName);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        List<ProgramEducationDto> programEducationDtos = new ArrayList<>();
//        for (String file : files) {
//            List<String> fileParts = List.of(file.replace(".txt", "").split("_"));
//            String universityCode = fileParts.get(1);
//            String programEducationCode = fileParts.get(2);
//            ProgramEducation program = programEducationRepository.findByUniversityCodeAndProgramEducationCode(universityCode, programEducationCode);
//            if (program == null) continue;
//            if (!Objects.equals(program.getId(), id))
//                programEducationDtos.add(modelMapper.map(program, ProgramEducationDto.class));
//        }
//        BaseResponse<List<ProgramEducationDto>> baseResponse = new BaseResponse<>();
//        baseResponse.setData(programEducationDtos);
//        baseResponse.success();
//        return baseResponse;
    }


    public BaseResponse<List<ProgramEducationDto>> getAllByUser(HttpServletRequest request, QueryParams params) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));
        return getAllByUniversityId(university.getId(), params);

    }
}
