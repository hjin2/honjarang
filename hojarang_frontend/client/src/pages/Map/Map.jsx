import { useState, useEffect } from "react"
import {Map, MapMarker} from 'react-kakao-maps-sdk'
import { Link } from "react-router-dom";

const {kakao} = window;


function Map1() {
  const [info, setInfo] = useState()
  const [markers, setMarkers] = useState([])
  const [map, setMap] = useState()
  const[Keyword, setKeyword] = useState('')

  const onChange = (e) => {
    setKeyword(e.target.value)
  }

  const onSubmit = (e) => {
    e.preventDefault()
    
    if (!map) return
    const ps = new kakao.maps.services.Places()

    ps.keywordSearch(Keyword, (data, status, _pagination) => {
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

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
        map.setBounds(bounds)
      }
      else {
        alert('검색결과가 없습니다.')
      }
    })
  }
  
  useEffect(() => {
    
    if (!map) return
    const ps = new kakao.maps.services.Places()

    ps.keywordSearch(Keyword, (data, status, _pagination) => {
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

        // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
        map.setBounds(bounds)
      }
    })
  }, [map])
  
  return (
    <div>
      <form onSubmit={onSubmit}>
        <input type="text" onChange={onChange}/>
        <button>검색</button>
        </form>
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
    <Link to={'/map2'}> 
    Map2로 이동
    </Link>
    </div>
  )
}

export default Map1