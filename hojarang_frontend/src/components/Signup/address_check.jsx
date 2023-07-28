import Post from "./Post"
import { Link } from "react-router-dom"

function Address_check() {

  return (
    <div>
      <div>
            주소
            <br />
            <input type="text" />
            <button className='border-solid border border-black rounded bg-gray2 ml-2'>
              <Link to='/post' target="_blank" element={<Post />}>주소 검색</Link></button>
          </div>
          <div>
            상세주소
            <br />
            <input type="text" id="address2"/>
          </div>
    </div>
  )
}


export default Address_check