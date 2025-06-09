package nova.backend.global.util;

import jakarta.annotation.PostConstruct;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class NicknameGenerator {

    private List<String> cafeVerbs;
    private List<String> ecoNouns;
    private final Random random = new Random();

    private List<String> loadKeywords(String path) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(new ClassPathResource(path).getInputStream(), StandardCharsets.UTF_8))) {
            return reader.lines().filter(line -> !line.trim().isEmpty()).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("키워드 파일 로딩 실패: " + path, e);
        }
    }

    public String generateNickname() {
        String verb = cafeVerbs.get(random.nextInt(cafeVerbs.size()));
        String noun = ecoNouns.get(random.nextInt(ecoNouns.size()));
        return verb + noun;
    }

    @PostConstruct
    public void init() {
        cafeVerbs = loadKeywords("keywords/cafe_verbs.txt");
        ecoNouns = loadKeywords("keywords/eco_nouns.txt");
    }

}
