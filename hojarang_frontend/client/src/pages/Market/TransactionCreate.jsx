import {useState} from 'react'
import axios from 'axios'
import {useNavigate} from 'react-router-dom'

export default function TransactionCreate() {
  const navigate = useNavigate()
  const URL = import.meta.env.VITE_APP_API
  const token = localStorage.getItem("access_token")
  const headers = {
    "Authorization" : `Bearer ${token}`,
    "Content-Type" : "multipart/formed-data"
  }
  const [title, setTitle] = useState('')
  const [price, setPrice] = useState(0)
  const [content, setContent] = useState('')
  const [image, setImage] = useState()
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
  const saveTransaction = () =>{
    const formData = new FormData()
    formData.append("transaction_image", image)
    formData.append("title", title)
    formData.append("content", content)
    formData.append("price", price)
    console.log(image,title,content,price)
    axios.post(`${URL}/api/v1/secondhand-transactions`,formData,{headers})
      .then((res) =>{
        console.log(res)
        navigate(`/market/transactiondetail/${res.data}`,{replace:true})
      })
      .catch((err)=>console.log(err))
  }

  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
      <div>
        <div>상품명</div>
        <input type="text" onChange={handleTitle} />
      </div>
      <div>
        <div>상품 가격</div>
        <input type="Number" onChange={handlePrice} />
      </div>
      <div>
        <div>상품 소개</div>
        <textarea className="resize-none border border-black h-48 w-full " onChange={handleContent}></textarea>
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
          onClick={saveTransaction}
        >작성완료</button>
      </div>
    </div>
  )
}