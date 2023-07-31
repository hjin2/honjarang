import React, {useState, useEffect} from "react";
import { useParams } from "react-router-dom";

function DeliveryDetail() {
  const params = useParams()
  const [Detail, setDetail] = useState({
    id: 16, name: 'BBQ', menu: '메뉴'
  })

  useEffect(() => {
    fetch(`http://localhost8080://api/v1/joint-delivery/${params.id}`)
    .then((res) => res.json())
    .then((data) => setDetail(data));
  })


  return(
    <div>
      {Detail.id}
      {Detail.name}
      {Detail.menu}
    </div>
  )
}

export default DeliveryDetail