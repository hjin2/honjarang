package com.example.honjarang.domain.jointdelivery.service;

import com.example.honjarang.domain.jointdelivery.dto.CreateJoinDeliveryDto;
import com.example.honjarang.domain.jointdelivery.dto.MenuListDto;
import com.example.honjarang.domain.jointdelivery.dto.StoreDto;
import com.example.honjarang.domain.jointdelivery.dto.StoreListDto;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.jointdelivery.exception.JointDeliveryNotFoundException;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryRepository;
import com.example.honjarang.domain.jointdelivery.repository.MenuListDtoRepository;
import com.example.honjarang.domain.jointdelivery.repository.StoreRepository;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RequiredArgsConstructor
@Service
public class JointDeliveryService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final StoreRepository storeRepository;
    private final MenuListDtoRepository menuListDtoRepository;
    private final JointDeliveryRepository jointDeliveryRepository;

    public List<StoreListDto> getStoreListByApi(String keyword) {
        String url = "https://map.naver.com/v5/api/search";

        URI targetUrl = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("query", keyword)
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(targetUrl, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            if(jsonNode.get("result").get("place") == null) {
                throw new RuntimeException("장소를 찾을 수 없습니다.");
            }
            JsonNode storeList = jsonNode.get("result").get("place").get("list");
            List<StoreListDto> storeListDtoList = new ArrayList<>();
            for(int i = 0; i < storeList.size(); i++) {
                JsonNode store = storeList.get(i);
                StoreListDto storeListDto = new StoreListDto();
                storeListDto.setId(store.get("id").asLong());
                storeListDto.setName(store.get("name").asText());
                storeListDto.setImage(store.get("thumUrl").asText());
                storeListDto.setAddress(store.get("roadAddress").asText());
                storeListDtoList.add(storeListDto);
            }
            return storeListDtoList;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public StoreDto getStore(Long storeId) {
        String url = "https://map.naver.com/v5/api/sites/summary/" + storeId +"?lang=ko";
        ResponseEntity<String> responseEntity = restTemplate.getForEntity(url, String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            StoreDto storeDto = new StoreDto();
            storeDto.setId(jsonNode.get("id").asLong());
            storeDto.setName(jsonNode.get("name").asText());
            storeDto.setImage(jsonNode.get("imageURL").asText());
            storeDto.setAddress(jsonNode.get("fullRoadAddress").asText());
            storeDto.setLatitude(jsonNode.get("y").asDouble());
            storeDto.setLongitude(jsonNode.get("x").asDouble());
            return storeDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public List<MenuListDto> getMenuList(Long deliveryId) {
        JointDelivery jointDelivery = jointDeliveryRepository.findById(deliveryId).orElseThrow(() -> new JointDeliveryNotFoundException("해당 공동배달이 존재하지 않습니다."));
        return menuListDtoRepository.findAllByStoreId(jointDelivery.getStore().getId());
    }

    public List<MenuListDto> getMenuListByApi(Long storeId) {
        List<MenuListDto> menuListDtoList = new ArrayList<>();
        String html = fetchHtmlByStoreId(storeId);
        Document document = Jsoup.parse(html);
        Element script = document.select("script").get(2);
        String scriptData = script.data();
        String[] splitData = scriptData.split("window.__APOLLO_STATE__ = ");
        String[] splitData2 = splitData[1].split(";");
        try {
            JsonNode jsonNode = objectMapper.readTree(splitData2[0]);
            Iterator<String> iterator = jsonNode.fieldNames();
            while (iterator.hasNext()) {
                String key = iterator.next();
                if (key.startsWith("Menu")) {
                    JsonNode menu = jsonNode.get(key);
                    MenuListDto menuListDto = new MenuListDto();
                    menuListDto.setStoreId(storeId);
                    menuListDto.setName(menu.get("name").asText());
                    menuListDto.setPrice(menu.get("price").asInt());
                    menuListDto.setImage(menu.get("images").get(0).asText());
                    menuListDtoList.add(menuListDto);
                }
            }
            return menuListDtoList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void createJointDelivery(CreateJoinDeliveryDto createJoinDeliveryDto, User user) {
        StoreDto storeDto = getStore(createJoinDeliveryDto.getStoreId());
        Store store = Store.builder()
                .id(storeDto.getId())
                .storeName(storeDto.getName())
                .image(storeDto.getImage())
                .address(storeDto.getAddress())
                .latitude(storeDto.getLatitude())
                .longitude(storeDto.getLongitude())
                .build();
        storeRepository.save(store);

        List<MenuListDto> menuListDtoList = getMenuListByApi(createJoinDeliveryDto.getStoreId());
        menuListDtoRepository.saveAll(menuListDtoList);
        jointDeliveryRepository.save(createJoinDeliveryDto.toEntity(createJoinDeliveryDto, store, user));
    }

    private String fetchHtmlByStoreId(Long storeId) {
        String url = "https://pcmap.place.naver.com/restaurant/" + storeId + "/home";
        try {
            Document document = Jsoup.connect(url).get();
            return document.outerHtml();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
