package com.web6.server.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.web6.server.dto.MovieRequestVo;
import com.web6.server.dto.MovieResponseVo;
import com.web6.server.dto.ResponseVo;
import com.web6.server.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MovieController {
    private final MovieService movieService;

    @GetMapping("/movie/findAll")
    public ResponseEntity<ResponseVo> testOpenAPI() {

        //requestVo 정의
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("N");
        movieRequestVo.setListCount(50);

        // controller에선 request/response만 참여
        // 실제 데이터 처리 비즈니스 로직은 service로 이동
        String movieResponse = movieService.getMovieInfoList(movieRequestVo);
        log.info("json String response : " + movieResponse);

        // ObjectMapper를 이용해서 json 문자열을 MovieResponseVo 객체로 매핑
        ObjectMapper objectMapper = new ObjectMapper();


        MovieResponseVo response = null;
        try {
            response = objectMapper.readValue(movieResponse, MovieResponseVo.class);
            // 매핑된 객체를 콘솔에 출력
            System.out.println(response);
            log.info("MovieResponseVo : " + response);
        } catch (JsonMappingException e) {
            // 매핑 중에 문제가 발생한 경우
            log.error("Error occurred while mapping JSON to object: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            // JSON 파싱 중에 문제가 발생한 경우
            log.error("Error occurred while processing JSON: " + e.getMessage(), e);
        } catch (Exception e) {
            // 기타 예외가 발생한 경우
            log.error("Unexpected error occurred: " + e.getMessage(), e);
        }

        ResponseVo responseVo = new ResponseVo();
        if (response.getData().isEmpty()) {
            responseVo.setUcd("99");
            responseVo.setMessage("영화 조회 실패");
        } else {
            responseVo.setUcd("00");
            responseVo.setMessage(response.toString());
        }
        return ResponseEntity.ok(responseVo);
    }


    /* 검색 기능 */
    @GetMapping("/movie/search")
    public ResponseEntity<ResponseVo> searchMovies(@RequestParam(required = false) String query,
                                                   @RequestParam(required = false) String title,
                                                   @RequestParam(required = false) String genre,
                                                   @RequestParam(required = false) String actor,
                                                   @RequestParam(required = false) String director,
                                                   @RequestParam(required = false) String nation,
                                                   @RequestParam(required = false) String keyword) {

        // 검색어를 MovieRequestVo에 설정
        MovieRequestVo movieRequestVo = new MovieRequestVo();
        movieRequestVo.setServiceKey("MZ6960ZIAJY0W0XX7IX7");
        movieRequestVo.setDetail("N");
        movieRequestVo.setListCount(50);

        if (query != null) {
            movieRequestVo.setQuery(query);
        }

        if (title != null) {
            movieRequestVo.setTitle(title);
        }

        if (genre != null) {
            movieRequestVo.setGenre(genre);
        }

        if (actor != null) {
            movieRequestVo.setActor(actor);
        }

        if (director != null) {
            movieRequestVo.setDirector(director);
        }

        if (nation != null) {
            movieRequestVo.setNation(nation);
        }

        if (keyword != null) {
            movieRequestVo.setKeyword(keyword);
        }

        // 영화 정보를 가져오는 서비스 호출
        String movieResponse = movieService.getMovieInfoList(movieRequestVo);
        //log.info("json String response : " + movieResponse);

        // JSON을 MovieResponseVo 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        MovieResponseVo response = null;
        try {
            response = objectMapper.readValue(movieResponse, MovieResponseVo.class);
            log.info("MovieResponseVo : " + response);
        } catch (JsonMappingException e) {
            log.error("Error occurred while mapping JSON to object: " + e.getMessage(), e);
        } catch (JsonProcessingException e) {
            log.error("Error occurred while processing JSON: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error occurred: " + e.getMessage(), e);
        }

        // 응답 생성
        ResponseVo responseVo = new ResponseVo();
        if (response == null || response.getData().isEmpty()) {
            responseVo.setUcd("99");
            responseVo.setMessage("영화 조회 실패");
        } else {
            responseVo.setUcd("00");
            responseVo.setMessage(response.toString());
        }
        return ResponseEntity.ok(responseVo);
    }
}
