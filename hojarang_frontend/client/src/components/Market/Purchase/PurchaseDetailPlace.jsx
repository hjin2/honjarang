import React from 'react'
import {Map, MapMarker} from 'react-kakao-maps-sdk'

export default function PurchaseDetailPlace({ latitude, longitude, placeName }) {
  
  return (
    <div>
      <Map // 로드뷰를 표시할 Container
        center={{
            lat: latitude,
            lng: longitude,
        }}
        style={{
          width: "100%",
          height: "350px",
        }}
        level={3}
        // onCreate={setMap} 
        className="z-0" 
      >
      <MapMarker 
        position={{lat:latitude,lng:longitude}}
        image={{
          src: "https://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_red.png", // 마커이미지의 주소입니다
          size: {
            width: 32,
            height: 32,
          },
        }}
      >
        <div>{placeName}</div>
      </MapMarker>
    </Map>
    </div>
  )
}
