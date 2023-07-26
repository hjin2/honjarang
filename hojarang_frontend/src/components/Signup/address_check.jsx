
function Address() {
  

  return (
    <div>
      <div>
            주소
            <br />
            <input type="text" />
            <button className='border-solid border border-black rounded bg-gray2 ml-2'>주소 검색</button>
          </div>
          <div>
            상세주소
            <br />
            <input type="text" id="address2"/>
          </div>
    </div>
  )
}


export default Address