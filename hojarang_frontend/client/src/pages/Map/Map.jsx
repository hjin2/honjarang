import { useState, useEffect } from "react"
import {Map, MapMarker} from 'react-kakao-maps-sdk'
import axios from "axios";

const {kakao} = window;


function Map2() {
  const [info, setInfo] = useState()
  const [markers, setMarkers] = useState([])
  const [map, setMap] = useState()
  const [Keyword, setKeyword] = useState('')
  const [extractedText, setextractedText] = useState('')



  const onClick = (e) => {
    setKeyword(e.currentTarget.id)
    console.log(extractedText + ' ' + e.currentTarget.id)
    if (!map) return
    const ps = new kakao.maps.services.Places()

    ps.keywordSearch(extractedText + ' ' + e.currentTarget.id, (data, status, _pagination) => {
      if (status === kakao.maps.services.Status.OK) {
        // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
        // LatLngBounds 객체에 좌표를 추가합니다
        const bounds = new kakao.maps.LatLngBounds()

        let markers = []

        for (var i = 0; i < data.length; i++) {
          // @ts-ignore
          markers.push({
            position: {
              lat: data[i].y,
              lng: data[i].x,
            },
            content: data[i].place_name,
          })
          // @ts-ignore
          bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x))
        }

        setMarkers(markers)
        console.log(markers)
        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
        map.setBounds(bounds)
      }
      else {
        alert('검색결과가 없습니다.')
      }
    })
  }
  
  useEffect(() => {
    
    const id = localStorage.getItem('user_id')
    const token = localStorage.getItem('access_token')
    axios.get(`${import.meta.env.VITE_APP_API}/api/v1/users/info`,
    {
      params: {id: id},
      headers: {
        'Authorization' : `Bearer ${token}`
      }
    })
    .then((res) => {
      console.log(res.data)
      const startIndex = res.data.address.indexOf("("); // "("의 인덱스 찾기
      setextractedText(startIndex !== -1 ? res.data.address.substring(0, startIndex).trim() : res.data.address)
    })
  },[])



  // useEffect(() => {

   
    
  //   if (!map) return
  //   const ps = new kakao.maps.services.Places()

  //   ps.keywordSearch(Keyword, (data, status, _pagination) => {
  //     if (status === kakao.maps.services.Status.OK) {
  //       // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
  //       // LatLngBounds 객체에 좌표를 추가합니다
  //       const bounds = new kakao.maps.LatLngBounds()
  //       let markers = []

  //       for (var i = 0; i < data.length; i++) {
  //         // @ts-ignore
  //         markers.push({
  //           position: {
  //             lat: data[i].y,
  //             lng: data[i].x,
  //           },
  //           content: data[i].place_name,
  //         })
  //         // @ts-ignore
  //         bounds.extend(new kakao.maps.LatLng(data[i].y, data[i].x))
  //       }
  //       setMarkers(markers)
  //       console.log(markers)

  //       // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
  //       map.setBounds(bounds)
  //     }
  //   })
  // }, [map])
  
  return (
    <div>
    <Map // 로드뷰를 표시할 Container
    center={{
      lat: 37.566826,
      lng: 126.9786567,
      }}
      style={{
        width: "100%",
        height: "350px",
      }}
      level={3}
      onCreate={setMap}
      >
      {markers.map((marker) => (
        <MapMarker
        key={`marker-${marker.content}-${marker.position.lat},${marker.position.lng}`}
        position={marker.position}
        onClick={() => setInfo(marker)}
        >
          {info &&info.content === marker.content && (
            <div style={{color:"#000"}}>{marker.content}</div>
            )}
        </MapMarker>
      ))}
    </Map>
    <ul id="category" className="flex relative w-4/5 top-10 left-10 border-1 border-solid border-gray-400 rounded-5 shadow-md bg-white overflow-hidden z-2">
      <li id="은행" data-order="0" className="float-left w-50px px-6 py-0 text-center cursor-pointer" onClick={onClick}>
        <span className="category_bg bank"></span>
        은행
      </li>
      <li id="마트" data-order="1" className="float-left w-50px px-6 py-0 text-center cursor-pointer" onClick={onClick}>
        <span className="category_bg mart"></span>
        마트
      </li>
      <li id="약국" data-order="2" className="float-left w-50px px-6 py-0 text-center cursor-pointer" onClick={onClick}>
        <span className="category_bg pharmacy"></span>
        약국
      </li>
      <li id="카페" data-order="4" className="float-left w-50px px-6 py-0 text-center cursor-pointer" onClick={onClick}>
        <span className="category_bg cafe"></span>
        카페
      </li>
      <li id="편의점" data-order="5" className="float-left w-50px px-6 py-0 text-center cursor-pointer" onClick={onClick}>
        <span className="category_bg store"></span>
        편의점
      </li>
    </ul>
    </div>
  )
}

export default Map2