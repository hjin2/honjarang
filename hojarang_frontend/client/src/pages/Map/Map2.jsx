import React, { useEffect } from 'react';

const Map2 = () => {
  useEffect(() => {
    // 카카오 맵 API 로드
    const script = document.createElement('script');
    script.async = true;
    script.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=82bfda13c7d21bd293a9039f803f600f&libraries=services`;
    document.head.appendChild(script);

    script.onload = () => {
      // 카카오 맵 초기화 및 설정
      const mapContainer = document.getElementById('map');
      const mapOption = {
        center: new window.kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
        level: 5, // 지도의 확대 레벨
      };
      const map = new window.kakao.maps.Map(mapContainer, mapOption);

      // 장소 검색 객체 생성
      const ps = new window.kakao.maps.services.Places(map);

      // 마커를 클릭했을 때 해당 장소의 상세정보를 보여줄 커스텀 오버레이
      const placeOverlay = new window.kakao.maps.CustomOverlay({ zIndex: 1 });
      const contentNode = document.createElement('div');
      contentNode.className = 'placeinfo_wrap';
      placeOverlay.setContent(contentNode);

      // 마커와 검색결과 항목 클릭 이벤트 등록
      function addEventHandle(target, type, callback) {
        if (target.addEventListener) {
          target.addEventListener(type, callback);
        } else {
          target.attachEvent('on' + type, callback);
        }
      }

      // 클릭한 마커에 대한 장소 상세정보를 커스텀 오버레이로 표시하는 함수
      function displayPlaceInfo(place) {
        const content = `
          <div class="placeinfo">
            <a class="title" href="${place.place_url}" target="_blank" title="${place.place_name}">
              ${place.place_name}
            </a>
            ${place.road_address_name ? `
              <span title="${place.road_address_name}">${place.road_address_name}</span>
              <span class="jibun" title="${place.address_name}">(지번 : ${place.address_name})</span>
            ` : `
              <span title="${place.address_name}">${place.address_name}</span>
            `}
            <span class="tel">${place.phone}</span>
          </div>
          <div class="after"></div>
        `;

        contentNode.innerHTML = content;
        placeOverlay.setPosition(new window.kakao.maps.LatLng(place.y, place.x));
        placeOverlay.setMap(map);
      }

      // 지도에 idle 이벤트 등록
      window.kakao.maps.event.addListener(map, 'idle', searchPlaces);

      // 카테고리 검색 요청 함수
      function searchPlaces() {
        if (!currCategory) {
          return;
        }

        placeOverlay.setMap(null);
        removeMarker();

        ps.categorySearch(currCategory, placesSearchCB, { useMapBounds: true });
      }

      // 장소 검색 완료 콜백 함수
      function placesSearchCB(data, status, pagination) {
        if (status === window.kakao.maps.services.Status.OK) {
          displayPlaces(data);
        } else if (status === window.kakao.maps.services.Status.ZERO_RESULT) {
          // 검색결과가 없는 경우
        } else if (status === window.kakao.maps.services.Status.ERROR) {
          // 에러로 인해 검색결과가 나오지 않은 경우
        }
      }

      // 지도에 마커 표출 함수
      function displayPlaces(places) {
        const order = document.getElementById(currCategory).getAttribute('data-order');

        for (let i = 0; i < places.length; i++) {
          const marker = addMarker(new window.kakao.maps.LatLng(places[i].y, places[i].x), order);

          (function (marker, place) {
            window.kakao.maps.event.addListener(marker, 'click', function () {
              displayPlaceInfo(place);
            });
          })(marker, places[i]);
        }
      }

      // 마커 생성 및 지도 위에 표시 함수
      function addMarker(position, order) {
        const imageSrc = 'https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_category.png';
        const imageSize = new window.kakao.maps.Size(27, 28);
        const imgOptions = {
          spriteSize: new window.kakao.maps.Size(72, 208),
          spriteOrigin: new window.kakao.maps.Point(46, order * 36),
          offset: new window.kakao.maps.Point(11, 28),
        };
        const markerImage = new window.kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions);
        const marker = new window.kakao.maps.Marker({
          position: position,
          image: markerImage,
        });

        marker.setMap(map);
        markers.push(marker);

        return marker;
      }

      // 지도 위에 표시되고 있는 마커 제거 함수
      function removeMarker() {
        for (let i = 0; i < markers.length; i++) {
          markers[i].setMap(null);
        }
        markers = [];
      }

      // 카테고리 클릭 이벤트 등록
      function addCategoryClickEvent() {
        const category = document.getElementById('category');
        const children = category.children;

        for (let i = 0; i < children.length; i++) {
          children[i].onclick = onClickCategory;
        }
      }

      // 카테고리 클릭 이벤트 함수
      let currCategory = '';
      function onClickCategory() {
        const id = this.id;
        const className = this.className;

        placeOverlay.setMap(null);

        if (className === 'on') {
          currCategory = '';
          changeCategoryClass();
          removeMarker();
        } else {
          currCategory = id;
          changeCategoryClass(this);
          searchPlaces();
        }
      }

      // 카테고리 클릭 시 스타일 변경 함수
      function changeCategoryClass(el) {
        const category = document.getElementById('category');
        const children = category.children;

        for (let i = 0; i < children.length; i++) {
          children[i].className = '';
        }

        if (el) {
          el.className = 'on';
        }
      }

      // 각 카테고리에 클릭 이벤트 등록
      addCategoryClickEvent();
    };
  }, []);

  return (
    <div>
    <div className="map_wrap">
    <div id="map" style={{ width: '100%', height: '500px', position: 'relative', overflow: 'hidden' }}></div>
    <ul id="category" className="flex relative top-10 left-10 border-1 border-solid border-gray-400 rounded-5 shadow-md bg-white overflow-hidden z-2">
      <li id="BK9" data-order="0" className="float-left w-50px px-6 py-0 text-center cursor-pointer">
        <span className="category_bg bank"></span>
        은행
      </li>
      <li id="MT1" data-order="1" className="float-left w-50px px-6 py-0 text-center cursor-pointer">
        <span className="category_bg mart"></span>
        마트
      </li>
      <li id="PM9" data-order="2" className="float-left w-50px px-6 py-0 text-center cursor-pointer">
        <span className="category_bg pharmacy"></span>
        약국
      </li>
      <li id="OL7" data-order="3" className="float-left w-50px px-6 py-0 text-center cursor-pointer">
        <span className="category_bg oil"></span>
        주유소
      </li>
      <li id="CE7" data-order="4" className="float-left w-50px px-6 py-0 text-center cursor-pointer">
        <span className="category_bg cafe"></span>
        카페
      </li>
      <li id="CS2" data-order="5" className="float-left w-50px px-6 py-0 text-center cursor-pointer">
        <span className="category_bg store"></span>
        편의점
      </li>
    </ul>
  </div>
  </div>
  );
};

export default Map2;
