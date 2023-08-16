import {useState, useEffect} from 'react'
import axios from 'axios'
import {useNavigate, useParams} from 'react-router-dom'
import { API } from '@/apis/config'
export default function TransactionUpdate() {
	const {id} = useParams()
  const navigate = useNavigate()
  const token = localStorage.getItem("access_token")
  const headers = {
    "Authorization" : `Bearer ${token}`,
    "Content-Type" : "multipart/formed-data"
  }
  const [title, setTitle] = useState('')
  const [price, setPrice] = useState(0)
  const [content, setContent] = useState('')
  const [image, setImage] = useState()

	useEffect(()=>{
    axios.get(`${API.SECONDHAND}/${id}`,{headers})
      .then((res) => {
        console.log(res.data)
        setTitle(res.data.title)
				setContent(res.data.content)
				setPrice(res.data.price)
				setImage(res.data.image)
			})
      .catch((err) => console.log(err))
  },[])

  const handleTitle = (e) =>{
    setTitle(e.target.value)
  }
  const handleContent = (e) =>{
    setContent(e.target.value)
  }
  const handlePrice = (e) =>{
    const InputPrice = e.target.value
    if(InputPrice>0){
      setPrice(InputPrice)
    }else{
      setPrice(0)
    }
  }
  const handleImage = (e) =>{
    if(e.target.files[0]){
      setImage(e.target.files[0])
    }
  }
  const editTransaction = () =>{
    const formData = new FormData()
    // formData.append("transaction_image", image)
    formData.append("title", title)
    formData.append("content", content)
    formData.append("price", price)
    axios.put(`${API.SECONDHAND}`,formData,{headers})
      .then((res) =>{
        console.log(res)
        navigate(`/market/transactiondetail/${id}`,{replace:true})
      })
      .catch((err)=>console.log(err))
  }

  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
      <div>
        <div>상품명</div>
        <input type="text" onChange={handleTitle} value={title}/>
      </div>
      <div>
        <div>상품 가격</div>
        <input type="number" onChange={handlePrice} value={price}/>
      </div>
      <div>
        <div>상품 소개</div>
        <textarea className="resize-none border border-black h-48 w-full " onChange={handleContent} value={content}></textarea>
      </div>
      <div>
        <div>상품사진 첨부</div>
        <input 
          type="file"
          accept="image/jpg,image/png,image/jpeg"
          name="Transaction_image"
          onChange={handleImage}  
        />
      </div>
      <div>
        <button 
          type="button" 
          className="main1-full-button w-20"
          onClick={editTransaction}
        >수정완료</button>
      </div>
    </div>
  )
}
