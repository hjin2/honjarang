package com.example.honjarang.domain.jointdelivery.service;

import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.example.honjarang.domain.jointdelivery.dto.*;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.jointdelivery.exception.JointDeliveryNotFoundException;
import com.example.honjarang.domain.jointdelivery.exception.StoreNotFoundException;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryCartRepository;
import com.example.honjarang.domain.jointdelivery.repository.JointDeliveryRepository;
import com.example.honjarang.domain.jointdelivery.repository.MenuRepository;
import com.example.honjarang.domain.jointdelivery.repository.StoreRepository;
import com.example.honjarang.domain.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.domain.Pageable;
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
    private final MenuRepository menuRepository;
    private final JointDeliveryRepository jointDeliveryRepository;
    private final JointDeliveryCartRepository jointDeliveryCartRepository;

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
            if (jsonNode.get("result").get("place").isNull()) {
                throw new StoreNotFoundException("가게를 찾을 수 없습니다.");
            }
            JsonNode storeList = jsonNode.get("result").get("place").get("list");
            List<StoreListDto> storeListDtoList = new ArrayList<>();
            for (int i = 0; i < storeList.size(); i++) {
                JsonNode store = storeList.get(i);
                StoreListDto storeListDto = new StoreListDto();
                storeListDto.setId(store.get("id").asLong());
                storeListDto.setName(store.get("name").asText());
                storeListDto.setImage(store.get("thumUrl").asText());
                storeListDto.setAddress(store.get("roadAddress").asText());
                storeListDtoList.add(storeListDto);
            }
            return storeListDtoList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    private StoreDto getStoreByApi(Long storeId) {
        String url = "https://map.naver.com/v5/api/sites/summary/" + storeId + "?lang=ko";
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
    public List<MenuListDto> getMenuList(Long jointDeliveryId) {
        JointDelivery jointDelivery = jointDeliveryRepository.findById(jointDeliveryId).orElseThrow(() -> new JointDeliveryNotFoundException("해당 공동배달이 존재하지 않습니다."));
        List<Menu> menuList = menuRepository.findAllByStoreId(jointDelivery.getStore().getId());
        return menuList.stream()
                .map(MenuListDto::new)
                .toList();
    }

    private List<Menu> getMenuListByApi(Long storeId) {
        List<Menu> menuList = new ArrayList<>();
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
                    JsonNode menuNode = jsonNode.get(key);
                    Menu menu = Menu.builder()
                            .storeId(storeId)
                            .name(menuNode.get("name").asText())
                            .price(menuNode.get("price").asInt())
                            .image(menuNode.get("images").get(0).asText())
                            .build();
                    menuList.add(menu);
                }
            }
            return menuList;
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void createJointDelivery(JointDeliveryCreateDto jointDeliveryCreateDto, User user) {
        StoreDto storeDto = getStoreByApi(jointDeliveryCreateDto.getStoreId());
        Store store = Store.builder()
                .id(storeDto.getId())
                .storeName(storeDto.getName())
                .image(storeDto.getImage())
                .address(storeDto.getAddress())
                .latitude(storeDto.getLatitude())
                .longitude(storeDto.getLongitude())
                .build();
        storeRepository.save(store);

        List<Menu> menuListDtoList = getMenuListByApi(jointDeliveryCreateDto.getStoreId());
        menuRepository.saveAll(menuListDtoList);
        jointDeliveryRepository.save(jointDeliveryCreateDto.toEntity(jointDeliveryCreateDto, store, user));
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

    @Transactional(readOnly = true)
    public JointDeliveryDto getJointDelivery(Long jointDeliveryId) {
        JointDelivery jointDelivery = jointDeliveryRepository.findById(jointDeliveryId).orElseThrow(() -> new JointDeliveryNotFoundException("해당 공동배달이 존재하지 않습니다."));
        Integer currentTotalPrice = jointDeliveryCartRepository.findAllByJointDeliveryId(jointDeliveryId).stream()
                .map(jointDeliveryCart -> {
                    Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCart.getMenuId()))
                            .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));
                    return menu.getPrice() * jointDeliveryCart.getQuantity();
                })
                .reduce(0, Integer::sum);
        return new JointDeliveryDto(jointDelivery, currentTotalPrice);
    }

    @Transactional(readOnly = true)
    public List<JointDeliveryListDto> getJointDeliveryList(Integer page, Integer size) {
        Pageable pageable = Pageable.ofSize(size).withPage(page);
        List<JointDelivery> jointDeliveryList = jointDeliveryRepository.findAll(pageable).toList();
        return jointDeliveryList.stream()
                .map(jointDelivery -> {
                    Integer currentTotalPrice = jointDeliveryCartRepository.findAllByJointDeliveryId(jointDelivery.getId()).stream()
                            .map(jointDeliveryCart -> {
                                Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCart.getMenuId()))
                                        .orElseThrow(() -> new RuntimeException("메뉴를 찾을 수 없습니다."));
                                return menu.getPrice() * jointDeliveryCart.getQuantity();
                            })
                            .reduce(0, Integer::sum);
                    return new JointDeliveryListDto(jointDelivery, currentTotalPrice);
                })
                .toList();
    }

}
