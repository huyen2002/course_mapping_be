package com.example.course_mapping_be.services;

import com.example.course_mapping_be.constraints.LevelEducationType;
import com.example.course_mapping_be.constraints.RoleType;
import com.example.course_mapping_be.dtos.*;
import com.example.course_mapping_be.models.Major;
import com.example.course_mapping_be.models.ProgramEducation;
import com.example.course_mapping_be.models.University;
import com.example.course_mapping_be.models.User;
import com.example.course_mapping_be.repositories.*;
import com.example.course_mapping_be.security.JsonWebTokenProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.json.JSONObject;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Comparator;
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
    private final ProgramEducationCourseRepository programEducationCourseRepository;
    private final ComparableProgramEducationRepository comparableProgramEducationRepository;

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

//        if (programEducationRepository.existsByCode(programEducationDto.getCode())) {
//            throw new Exception("Code is already used");
//        }

        Major major = majorRepository.findById(programEducationDto.getMajorId()).orElseThrow(() -> new Exception("Major is not found"));
        String documentVector = null;
        String vectorName = documentService.convertDocumentToVectorDbow(programEducationDto.getName()).getData();
        if (programEducationDto.getOutline() != null) {
            String outline = readFileService.readData(programEducationDto.getOutline());
            documentVector = documentService.convertDocumentToVector(outline).getData();
        }

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
                .vectorOutline(documentVector)
                .vectorName(vectorName)
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


    public BaseResponse<ProgramEducationDto> update(Long id, ProgramEducationDto programEducationDto, HttpServletRequest request) throws Exception {
        ProgramEducation programEducation = programEducationRepository.findById(id).orElseThrow(() -> new Exception("Program education is not found"));
        Long userId = tokenProvider.getUserIdFromRequest(request);
        User user = userRepository.findById(userId).orElseThrow(() -> new Exception("User is not found"));
        if (user.getRole() != RoleType.UNIVERSITY && user.getRole() != RoleType.ADMIN) {
            throw new Exception("You are not authorized to update program education");
        }
        if (user.getRole() == RoleType.UNIVERSITY) {
            University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));
            if (!programEducation.getUniversity().getId().equals(university.getId())) {
                throw new Exception("You are not authorized to update this program education");
            }
        }

        if (programEducationDto.getName() != null) {
            programEducation.setName(programEducationDto.getName());
            programEducation.setVectorName(documentService.convertDocumentToVectorDbow(programEducationDto.getName()).getData());
        }
        if (programEducationDto.getCode() != null) {
            programEducation.setCode(programEducationDto.getCode());
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
            String data = readFileService.readData(programEducationDto.getOutline());
            String vectorDocument = documentService.convertDocumentToVector(data).getData();
            programEducation.setVectorOutline(vectorDocument);
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
        if (programEducationDto.getUniversityId() != null) {
            University university = universityRepository.findById(programEducationDto.getUniversityId()).orElseThrow(() -> new Exception("University is not found"));
            programEducation.setUniversity(university);
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
        programEducationCourseRepository.deleteByProgramId(id);
        comparableProgramEducationRepository.deleteByProgramId(id);
        programEducationRepository.deleteById(id);
        return true;

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

    public Float compareTwoVectorsFromString(String firstVector, String secondVector) {
        JSONObject jsonObject1 = new JSONObject(firstVector);
        JSONObject jsonObject2 = new JSONObject(secondVector);
        List<Object> vector1 = jsonObject1.getJSONArray("vector").toList();
        List<Object> vector2 = jsonObject2.getJSONArray("vector").toList();
        String uri = "http://localhost:5000/api/cossim_between_two_vectors";
        RestTemplate restTemplate = new RestTemplate();
        CompareTwoVectorsDto compareTwoVectorsDto = new CompareTwoVectorsDto(vector1, vector2);
        Float result = restTemplate.postForObject(uri, compareTwoVectorsDto, Float.class);
        return result;
    }

    public BaseResponse<Float> compareTwoPrograms(Long id1, Long id2) throws Exception {
        BaseResponse<Float> baseResponse = new BaseResponse<>();

        ProgramEducation programEducation1 = programEducationRepository.findById(id1).orElseThrow(() -> new Exception("Program education is not found"));
        ProgramEducation programEducation2 = programEducationRepository.findById(id2).orElseThrow(() -> new Exception("Program education is not found"));
        if (programEducation1.getVectorOutline() == null || programEducation2.getVectorOutline() == null) {
            Float similarity = compareTwoVectorsFromString(programEducation1.getVectorName(), programEducation2.getVectorName());
            baseResponse.setData(similarity);
            baseResponse.success();
            return baseResponse;
        }
        Float similarity = compareTwoVectorsFromString(programEducation1.getVectorOutline(), programEducation2.getVectorOutline());
        baseResponse.setData(similarity);
        baseResponse.success();
        return baseResponse;

    }

    public BaseResponse<List<Pair<ProgramEducationDto, Float>>> getTopSimilar(Long id, SortParam sortParam, FilterProgramParams filterProgramParams) throws Exception {
        ProgramEducation programEducation = programEducationRepository.findById(id).orElseThrow(() -> new Exception("Program education is not found"));
        List<ProgramEducation> programEducations = programEducationRepository.findAllByFilterParams(filterProgramParams);
        List<Pair<ProgramEducationDto, Float>> programEducationDtos = new ArrayList<>();
        // similarity between program education and other program educations and sort by similarity from high to low
        for (ProgramEducation program : programEducations) {
            if (!Objects.equals(program.getId(), id)) {
                Float similarity = compareTwoPrograms(id, program.getId()).getData();
                programEducationDtos.add(Pair.of(modelMapper.map(program, ProgramEducationDto.class), similarity));
            }

        }
        programEducationDtos.sort((o1, o2) -> {
            if (o1.getSecond() > o2.getSecond()) return -1;
            if (o1.getSecond() < o2.getSecond()) return 1;
            return 0;
        });
        BaseResponse<List<Pair<ProgramEducationDto, Float>>> baseResponse = new BaseResponse<>();
        switch (sortParam.getSortType()) {
            case SIMILARITY_DESC:
                // get top 10 most similar program educations
                baseResponse.setData(programEducationDtos.subList(0, Math.min(10, programEducationDtos.size())));
                break;
            case SIMILARITY_ASC:
                // get top 10 least similar program educations and sort by similarity from low to high
                List<Pair<ProgramEducationDto, Float>> result = programEducationDtos.subList(Math.max(0, programEducationDtos.size() - 10), programEducationDtos.size());
                // reverse the order of result
                List<Pair<ProgramEducationDto, Float>> reversedResult = new ArrayList<>();
                for (int i = result.size() - 1; i >= 0; i--) {
                    reversedResult.add(result.get(i));
                }
                baseResponse.setData(reversedResult);
                break;
            case ALPHABET_DESC:
                programEducationDtos.sort((o1, o2) -> {
                    if (o1.getFirst().getName().compareTo(o2.getFirst().getName()) > 0) return -1;
                    if (o1.getFirst().getName().compareTo(o2.getFirst().getName()) < 0) return 1;
                    return 0;
                });
                baseResponse.setData(programEducationDtos.subList(0, Math.min(10, programEducationDtos.size())));
                break;
            case ALPHABET_ASC:
                programEducationDtos.sort(Comparator.comparing(o -> o.getFirst().getName()));
                baseResponse.setData(programEducationDtos.subList(0, Math.min(10, programEducationDtos.size())));
                break;

            default:
                baseResponse.setData(programEducationDtos.subList(0, Math.min(10, programEducationDtos.size())));
                break;
        }
        baseResponse.success();
        return baseResponse;

    }


    public BaseResponse<List<ProgramEducationDto>> getAllByUser(HttpServletRequest request, QueryParams params) throws Exception {
        Long userId = tokenProvider.getUserIdFromRequest(request);
        University university = universityRepository.findByUserId(userId).orElseThrow(() -> new Exception("University is not found"));
        return getAllByUniversityId(university.getId(), params);

    }

    public BaseResponse<List<ProgramEducationDto>> filterProgramEducations(FilterProgramParams filterProgramParams) {
        BaseResponse<List<ProgramEducationDto>> baseResponse = new BaseResponse<>();
        List<ProgramEducation> programEducations = programEducationRepository.findAllByFilterParams(filterProgramParams);
        System.out.println(programEducations.size());
        List<ProgramEducationDto> programEducationDtos = new ArrayList<>();
        for (ProgramEducation programEducation : programEducations) {
            programEducationDtos.add(modelMapper.map(programEducation, ProgramEducationDto.class));
        }
        baseResponse.setData(programEducationDtos);
        baseResponse.success();
        baseResponse.updatePagination(new QueryParams(), (long) programEducations.size());
        return baseResponse;
    }

    public BaseResponse<ProgramEducationDto> updateEnabled(Long id, ProgramEducationDto programEducationDto) throws Exception {
        ProgramEducation programEducation = programEducationRepository.findById(id).orElseThrow(() -> new Exception("Program education is not found"));
        Boolean enabled = programEducationDto.getEnabled();
        if (enabled == null) {
            throw new Exception("Enabled is required");
        } else {
            programEducation.setEnabled(enabled);
        }
        programEducationRepository.save(programEducation);
        BaseResponse<ProgramEducationDto> baseResponse = new BaseResponse<>();
        baseResponse.setData(modelMapper.map(programEducation, ProgramEducationDto.class));
        baseResponse.success();
        return baseResponse;
    }

//    public BaseResponse<Boolean> existedByCode(String code) {
//        BaseResponse<Boolean> baseResponse = new BaseResponse<>();
//        baseResponse.setData(programEducationRepository.existsByCode(code));
//        baseResponse.success();
//        return baseResponse;
//    }
}
