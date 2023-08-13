package com.example.honjarang.domain.jointdelivery.service;

import com.example.honjarang.domain.chat.entity.ChatParticipant;
import com.example.honjarang.domain.chat.entity.ChatRoom;
import com.example.honjarang.domain.chat.exception.ChatParticipantNotFoundException;
import com.example.honjarang.domain.chat.exception.ChatRoomNotFoundException;
import com.example.honjarang.domain.chat.repository.ChatParticipantRepository;
import com.example.honjarang.domain.chat.repository.ChatRoomRepository;
import com.example.honjarang.domain.jointdelivery.document.Menu;
import com.example.honjarang.domain.jointdelivery.dto.*;
import com.example.honjarang.domain.jointdelivery.entity.JointDelivery;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryApplicant;
import com.example.honjarang.domain.jointdelivery.entity.JointDeliveryCart;
import com.example.honjarang.domain.jointdelivery.entity.Store;
import com.example.honjarang.domain.jointdelivery.exception.*;
import com.example.honjarang.domain.jointdelivery.repository.*;
import com.example.honjarang.domain.map.service.MapService;
import com.example.honjarang.domain.user.entity.User;
import com.example.honjarang.domain.user.exception.InsufficientPointsException;
import com.example.honjarang.domain.user.exception.UserNotFoundException;
import com.example.honjarang.domain.user.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.hibernate.mapping.Join;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@Service
public class JointDeliveryService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final MenuRepository menuRepository;
    private final JointDeliveryRepository jointDeliveryRepository;
    private final JointDeliveryCartRepository jointDeliveryCartRepository;
    private final JointDeliveryApplicantRepository jointDeliveryApplicantRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatParticipantRepository chatParticipantRepository;
    private final MapService mapService;

    public List<StoreListDto> getStoreListByApi(String keyword) {
        String url = "https://map.naver.com/v5/api/search";

        HttpHeaders headers = new HttpHeaders();
        headers.set("referer", "https://map.naver.com/v5/search");

        URI targetUrl = UriComponentsBuilder
                .fromUriString(url)
                .queryParam("query", keyword)
                .queryParam("caller", "pcweb")
                .queryParam("type", "all")
                .queryParam("searchCoord", "126.97838878631592;37.56661019999999")
                .queryParam("page", "1")
                .queryParam("displayCount", "20")
                .queryParam("isPlaceRecommendationReplace", "true")
                .queryParam("lang", "ko")
                .build()
                .encode(StandardCharsets.UTF_8)
                .toUri();
        ResponseEntity<String> responseEntity = restTemplate.exchange(targetUrl, HttpMethod.GET, new HttpEntity<>(headers), String.class);

        try {
            JsonNode jsonNode = objectMapper.readTree(responseEntity.getBody());
            if (jsonNode.get("result").get("place").isNull()) {
                throw new StoreNotFoundException("가게를 찾을 수 없습니다.");
            }
            JsonNode storeList = jsonNode.get("result").get("place").get("list");
            List<StoreListDto> storeListDtoList = new ArrayList<>();
            for (JsonNode store : storeList) {
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

    public List<StoreListDto> getStoreListByApiForTest(String keyword) {
        List<StoreListDto> storeListDtoList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            StoreListDto storeListDto = new StoreListDto();
            storeListDto.setId(1638150004L);
            storeListDto.setName("BBQ치킨 인동재롱점");
            storeListDto.setImage("https://ldb-phinf.pstatic.net/20230515_91/1684150548650oQLHs_PNG/%B9%E8%B9%CE%2C_%B9%E8%B9%CE%BF%F8_%B7%CE%B0%ED_2.png");
            storeListDto.setAddress("경상북도 구미시 인동중앙로3길 29 영무메트로 상가 1층 106호");
            storeListDtoList.add(storeListDto);
        }
        return storeListDtoList;
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

    private StoreDto getStoreByApiForTest(Long storeId) {
        String json = """
                {
                         "id": 1638150004,
                         "name": "BBQ치킨 인동재롱점",
                         "imageURL": "https://ldb-phinf.pstatic.net/20230515_91/1684150548650oQLHs_PNG/%B9%E8%B9%CE%2C_%B9%E8%B9%CE%BF%F8_%B7%CE%B0%ED_2.png",
                         "fullRoadAddress": "경상북도 구미시 인동중앙로3길 29 영무메트로 상가 1층 106호",
                         "x": 128.418346217966,
                         "y": 36.10857936950589
                 }""";
        try {
            JsonNode jsonNode = objectMapper.readTree(json);
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
        List<Menu> menuList = menuRepository.findAllByJointDeliveryId(jointDeliveryId);
        return menuList.stream()
                .map(MenuListDto::new)
                .toList();
    }

    private List<Menu> getMenuListByApi(Long storeId, Long jointDeliveryId) {
        List<Menu> menuList = new ArrayList<>();
        String html = fetchHtmlByStoreId(storeId);
//        String html = fetchHtmlByStoreIdForTest(storeId);
        Document document = Jsoup.parse(html);
        Element script = document.select("script").get(2);
        String scriptData = script.data();
        String[] splitData = scriptData.split("window.__APOLLO_STATE__ = ");
        String[] splitData2 = splitData[1].split("window.__PLACE_STATE__");
        try {
            JsonNode jsonNode = objectMapper.readTree(splitData2[0]);
            Iterator<String> iterator = jsonNode.fieldNames();
            while (iterator.hasNext()) {
                String key = iterator.next();
                // 키의 앞자리 4개가 숫자인 경우
                if (key.startsWith("Menu") || key.substring(0, 4).matches("^[0-9]*$")) {
                    JsonNode menuNode = jsonNode.get(key);
                    Menu menu = Menu.builder()
                            .jointDeliveryId(jointDeliveryId)
                            .name(menuNode.get("name").asText())
                            .price(menuNode.get("price").asInt())
                            .image(menuNode.get("images").get(0) != null ? menuNode.get("images").get(0).asText() : null)
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
    public Long createJointDelivery(JointDeliveryCreateDto jointDeliveryCreateDto, User loginUser) {
        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        if (user.getPoint() < 1000) {
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }
        user.subtractPoint(1000);

        StoreDto storeDto = getStoreByApi(jointDeliveryCreateDto.getStoreId());
//        StoreDto storeDto = getStoreByApiForTest(jointDeliveryCreateDto.getStoreId());
        Store store = Store.builder()
                .id(storeDto.getId())
                .storeName(storeDto.getName())
                .image(storeDto.getImage())
                .address(storeDto.getAddress())
                .latitude(storeDto.getLatitude())
                .longitude(storeDto.getLongitude())
                .build();
        if(storeRepository.existsById(storeDto.getId())) {
            store = storeRepository.findById(storeDto.getId()).orElseThrow(() -> new StoreNotFoundException("가게를 찾을 수 없습니다."));
        } else {
            storeRepository.save(store);
        }

        JointDelivery jointDelivery = jointDeliveryRepository.save(jointDeliveryCreateDto.toEntity(store, user));
        List<Menu> menuList = getMenuListByApi(jointDeliveryCreateDto.getStoreId(), jointDelivery.getId());

        // storeId의 메뉴를 가지고 있으면 삭제
        menuRepository.saveAll(menuList);

        ChatRoom chatRoom = ChatRoom.builder()
                .name(jointDelivery.getId().toString() + "번 공동배달 채팅방")
                .build();
        chatRoomRepository.save(chatRoom);
        ChatParticipant chatParticipant = ChatParticipant.builder()
                .chatRoom(chatRoom)
                .user(user)
                .build();
        chatParticipantRepository.save(chatParticipant);
        return jointDelivery.getId();
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

    private String fetchHtmlByStoreIdForTest(Long storeId) {
        try {
            File file = new File("./test-menus.html");
            return Files.readString(file.toPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional(readOnly = true)
    public JointDeliveryDto getJointDelivery(Long jointDeliveryId, User loginUser) {
        JointDelivery jointDelivery = jointDeliveryRepository.findById(jointDeliveryId).orElseThrow(() -> new JointDeliveryNotFoundException("해당 공동배달이 존재하지 않습니다."));
        int currentTotalPrice = 0;
        List<JointDeliveryCart> jointDeliveryCartList = jointDeliveryCartRepository.findAllByJointDeliveryId(jointDeliveryId);
        for (JointDeliveryCart jointDeliveryCart : jointDeliveryCartList) {
            Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCart.getMenuId()))
                    .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));
            currentTotalPrice += menu.getPrice() * jointDeliveryCart.getQuantity();
        }
        return new JointDeliveryDto(jointDelivery, currentTotalPrice, loginUser.getPoint());
    }

    @Transactional(readOnly = true)
    public List<JointDeliveryListDto> getJointDeliveryList(Integer page, Integer size, String keyword, User loginUser) {
        Pageable pageable = Pageable.ofSize(size).withPage(page - 1);
        List<JointDelivery> jointDeliveryList = jointDeliveryRepository.findAllByIsCanceledFalseAndDeadlineAfterAndDistanceLessThanOrderByCreatedAtDesc(LocalDateTime.now(), loginUser.getLatitude(), loginUser.getLongitude(), keyword, pageable).toList();

        List<JointDeliveryListDto> jointDeliveryListDtoList = new ArrayList<>();

        for (JointDelivery jointDelivery : jointDeliveryList) {
            // 총 가격 계산
            int currentTotalPrice = 0;
            List<JointDeliveryCart> jointDeliveryCartList = jointDeliveryCartRepository.findAllByJointDeliveryId(jointDelivery.getId());
            for (JointDeliveryCart jointDeliveryCart : jointDeliveryCartList) {
                Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCart.getMenuId()))
                        .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));
                currentTotalPrice += menu.getPrice() * jointDeliveryCart.getQuantity();
            }

            JointDeliveryListDto jointDeliveryListDto = new JointDeliveryListDto(jointDelivery, currentTotalPrice);
            jointDeliveryListDtoList.add(jointDeliveryListDto);
        }
        return jointDeliveryListDtoList;
    }

    @Transactional
    public void cancelJointDelivery(Long jointDeliveryId, User loginUser) {
        JointDelivery jointDelivery = jointDeliveryRepository.findById(jointDeliveryId).orElseThrow(() -> new JointDeliveryNotFoundException("해당 공동배달이 존재하지 않습니다."));

        if (!jointDelivery.getUser().getId().equals(loginUser.getId())) {
            throw new UnauthorizedJointDeliveryAccessException("공동배달을 취소할 권한이 없습니다.");
        }
        if (jointDelivery.getIsCanceled()) {
            throw new JointDeliveryCanceledException("공동배달이 이미 취소되었습니다.");
        }

        if (jointDelivery.getDeadline().isAfter(LocalDateTime.now())) {
            jointDelivery.getUser().addPoint(1000);
        }

        List<JointDeliveryCart> jointDeliveryCartList = jointDeliveryCartRepository.findAllByJointDeliveryId(jointDeliveryId);
        for (JointDeliveryCart jointDeliveryCart : jointDeliveryCartList) {
            Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCart.getMenuId()))
                    .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));
            jointDeliveryCart.getUser().addPoint(menu.getPrice() * jointDeliveryCart.getQuantity());
        }
        jointDelivery.cancel();
    }

    @Transactional(readOnly = true)
    public List<JointDeliveryCartListDto> getJointDeliveryCartList(Long jointDeliveryId, User loginUser) {
        JointDelivery jointDelivery = jointDeliveryRepository.findById(jointDeliveryId).orElseThrow(() -> new JointDeliveryNotFoundException("해당 공동배달이 존재하지 않습니다."));

        if (!jointDelivery.getUser().getId().equals(loginUser.getId()) && !jointDeliveryApplicantRepository.existsByJointDeliveryIdAndUserId(jointDeliveryId, loginUser.getId())) {
            throw new JointDeliveryCartAccessException("장바구니에 접근할 수 없습니다.");
        }

        List<JointDeliveryCartListDto> jointDeliveryCartListDtoList = new ArrayList<>();
        List<JointDeliveryCart> jointDeliveryCartList = jointDeliveryCartRepository.findAllByJointDeliveryId(jointDeliveryId);
        for (JointDeliveryCart jointDeliveryCart : jointDeliveryCartList) {
            Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCart.getMenuId()))
                    .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));
            JointDeliveryCartListDto jointDeliveryCartListDto = new JointDeliveryCartListDto(jointDeliveryCart, menu);
            jointDeliveryCartListDtoList.add(jointDeliveryCartListDto);
        }
        return jointDeliveryCartListDtoList;
    }

    @Transactional
    public void addJointDeliveryCart(JointDeliveryCartCreateDto jointDeliveryCartCreateDto, User loginUser) {
        JointDelivery jointDelivery = jointDeliveryRepository.findById(jointDeliveryCartCreateDto.getJointDeliveryId()).orElseThrow(() -> new JointDeliveryNotFoundException("해당 공동배달이 존재하지 않습니다."));

        if (jointDelivery.getDeadline().isBefore(LocalDateTime.now())) {
            throw new JointDeliveryExpiredException("공동배달이 마감되었습니다.");
        }
        if (jointDelivery.getIsCanceled()) {
            throw new JointDeliveryCanceledException("공동배달이 취소되었습니다.");
        }

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCartCreateDto.getMenuId()))
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

        // 공동배달 주최자가 아닌 경우 포인트 차감
        if (!jointDelivery.getUser().getId().equals(loginUser.getId()) && user.getPoint() < menu.getPrice() * jointDeliveryCartCreateDto.getQuantity()) {
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }
        if (!jointDelivery.getUser().getId().equals(loginUser.getId())) {
            user.subtractPoint(menu.getPrice() * jointDeliveryCartCreateDto.getQuantity());
        }

        // 공동배달 주최자가 아니고 최초로 장바구니에 담는 경우
        if (!jointDelivery.getUser().getId().equals(loginUser.getId()) && !jointDeliveryCartRepository.existsByJointDeliveryIdAndUserId(jointDelivery.getId(), user.getId()) && user.getPoint() < 1000) {
            throw new InsufficientPointsException("포인트가 부족합니다.");
        }
        if (!jointDelivery.getUser().getId().equals(loginUser.getId()) && !jointDeliveryCartRepository.existsByJointDeliveryIdAndUserId(jointDelivery.getId(), user.getId())) {
            user.subtractPoint(1000);
            ChatRoom chatRoom = chatRoomRepository.findByName(jointDelivery.getId().toString() + "번 공동배달 채팅방").orElseThrow(() -> new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다."));
            ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomIdAndUserId(chatRoom.getId(), user.getId()).orElse(null);
            // 최초 입장인 경우
            if (chatParticipant == null) {
                chatParticipant = ChatParticipant.builder()
                        .chatRoom(chatRoom)
                        .user(user)
                        .build();
                chatParticipantRepository.save(chatParticipant);
            } else {
                chatParticipant.reEnter();
            }
        }

        // 해당 배달에서 이미 담은 똑같은 메뉴가 있다면 갯수만 더하기
        JointDeliveryCart jointDeliveryCart = jointDeliveryCartRepository.findByJointDeliveryIdAndMenuIdAndUserId(jointDelivery.getId(), jointDeliveryCartCreateDto.getMenuId(), user.getId()).orElse(null);
        if (jointDeliveryCart != null) {
            jointDeliveryCart.addQuantity(jointDeliveryCartCreateDto.getQuantity());
        } else {
            jointDeliveryCartRepository.save(jointDeliveryCartCreateDto.toEntity(jointDelivery, user));
        }

        if (!jointDeliveryApplicantRepository.existsByJointDeliveryIdAndUserId(jointDelivery.getId(), user.getId())) {
            jointDeliveryApplicantRepository.save(JointDeliveryApplicant.builder()
                    .jointDelivery(jointDelivery)
                    .user(user)
                    .build());
        }
    }

    @Transactional
    public void removeJointDeliveryCart(Long jointDeliveryCartId, User loginUser) {
        JointDeliveryCart jointDeliveryCart = jointDeliveryCartRepository.findById(jointDeliveryCartId).orElseThrow(() -> new JointDeliveryCartNotFoundException("해당 공동배달이 존재하지 않습니다."));

        if (jointDeliveryCart.getJointDelivery().getDeadline().isBefore(LocalDateTime.now())) {
            throw new JointDeliveryExpiredException("공동배달이 마감되었습니다.");
        }
        if (jointDeliveryCart.getJointDelivery().getIsCanceled()) {
            throw new JointDeliveryCanceledException("공동배달이 취소되었습니다.");
        }
        if (!jointDeliveryCart.getUser().getId().equals(loginUser.getId())) {
            throw new JointDeliveryCartAccessException("장바구니에 접근할 수 없습니다.");
        }

        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));
        Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCart.getMenuId()))
                .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

        // 공동배달 주최자가 아닌 경우 포인트 환급
        if (!jointDeliveryCart.getJointDelivery().getUser().getId().equals(loginUser.getId())) {
            user.addPoint(menu.getPrice() * jointDeliveryCart.getQuantity());
        }

        jointDeliveryCartRepository.delete(jointDeliveryCart);

        // 장바구니에 담긴 상품이 없는 경우
        if (!jointDeliveryCartRepository.existsByJointDeliveryIdAndUserId(jointDeliveryCart.getJointDelivery().getId(), user.getId())) {
            // 참석자에서 제거
            jointDeliveryApplicantRepository.deleteByJointDeliveryIdAndUserId(jointDeliveryCart.getJointDelivery().getId(), user.getId());
            // 주최자가 아닌 경우
            if (!jointDeliveryCart.getJointDelivery().getUser().getId().equals(loginUser.getId())) {
                user.addPoint(1000);
                ChatRoom chatRoom = chatRoomRepository.findByName(jointDeliveryCart.getJointDelivery().getId().toString() + "번 공동배달 채팅방").orElseThrow(() -> new ChatRoomNotFoundException("채팅방을 찾을 수 없습니다."));
                ChatParticipant chatParticipant = chatParticipantRepository.findByChatRoomIdAndUserId(chatRoom.getId(), user.getId()).orElseThrow(() -> new ChatParticipantNotFoundException("채팅방 참여자를 찾을 수 없습니다."));
                chatParticipant.exit();
            }
        }
    }

    @Transactional
    public void confirmReceived(Long jointDeliveryId, User loginUser) {
        JointDeliveryApplicant jointDeliveryApplicant = jointDeliveryApplicantRepository.findByJointDeliveryIdAndUserId(jointDeliveryId, loginUser.getId()).orElseThrow(() -> new JointDeliveryApplicantNotFoundException("공동배달 신청자가 아닙니다."));
        User user = userRepository.findById(loginUser.getId()).orElseThrow(() -> new UserNotFoundException("사용자를 찾을 수 없습니다."));

        if (jointDeliveryApplicant.getJointDelivery().getDeadline().isAfter(LocalDateTime.now())) {
            throw new JointDeliveryNotClosedException("공동배달이 마감되지 않았습니다.");
        }
        if (jointDeliveryApplicant.getJointDelivery().getIsCanceled()) {
            throw new JointDeliveryCanceledException("공동배달이 취소되었습니다.");
        }
        if (jointDeliveryApplicant.getIsReceived()) {
            throw new JointDeliveryAlreadyReceivedException("이미 수령확인을 하였습니다.");
        }

        jointDeliveryApplicant.confirmReceived();

        List<JointDeliveryCart> jointDeliveryCartList = jointDeliveryCartRepository.findAllByJointDeliveryIdAndUserId(jointDeliveryId, loginUser.getId());

        // 공동배달 주최자인 경우 패스
        if (jointDeliveryApplicant.getJointDelivery().getUser().getId().equals(loginUser.getId())) {
            return;
        }

        // 공동배달 주최자에게 포인트 지급
        Integer totalPrice = 0;
        for (JointDeliveryCart jointDeliveryCart : jointDeliveryCartList) {
            Menu menu = menuRepository.findById(new ObjectId(jointDeliveryCart.getMenuId()))
                    .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));
            totalPrice += menu.getPrice() * jointDeliveryCart.getQuantity();
        }
        jointDeliveryApplicant.getJointDelivery().getUser().addPoint(totalPrice);

        // 배달비 차액 환급
        Integer applicantCount = jointDeliveryApplicantRepository.countByJointDeliveryId(jointDeliveryId);
        int refundPoint = 1000 - jointDeliveryApplicant.getJointDelivery().getDeliveryCharge() / applicantCount;
        if (refundPoint > 0) {
            user.addPoint(refundPoint);
        }
    }

    @Transactional(readOnly = true)
    public List<JointDeliveryApplicantListDto> getJointDeliveryApplicantList(Long jointDeliveryId) {
        List<Object[]> queryResults = jointDeliveryApplicantRepository.findAllByJointDeliveryId(jointDeliveryId);
        Map<Long, JointDeliveryApplicantListDto> jointDeliveryApplicantListDtoMap = new HashMap<>();

        for (Object[] result : queryResults) {
            JointDeliveryApplicant jda = (JointDeliveryApplicant) result[0];
            JointDeliveryCart jdc = (JointDeliveryCart) result[1];
            Menu menu = menuRepository.findById(new ObjectId(jdc.getMenuId()))
                    .orElseThrow(() -> new MenuNotFoundException("메뉴를 찾을 수 없습니다."));

            if(jointDeliveryApplicantListDtoMap.containsKey(jda.getId())) {
                JointDeliveryApplicantListDto jointDeliveryApplicantListDto = jointDeliveryApplicantListDtoMap.get(jda.getId());
                jointDeliveryApplicantListDto.addTotalPrice(menu.getPrice() * jdc.getQuantity());
            } else {
                JointDeliveryApplicantListDto jointDeliveryApplicantListDto = new JointDeliveryApplicantListDto(jda, menu.getPrice() * jdc.getQuantity());
                jointDeliveryApplicantListDtoMap.put(jda.getId(), jointDeliveryApplicantListDto);
            }
        }
        return jointDeliveryApplicantListDtoMap.values().stream()
                .toList();
    }

    @Transactional(readOnly = true)
    public Integer getJointDeliveryPageCount(Integer size, String keyword) {
        return (int) Math.ceil((double) jointDeliveryRepository.countByIsCanceledFalseAndDeadlineAfterAndContentContainingIgnoreCase(LocalDateTime.now(), keyword) / size);
    }
}
