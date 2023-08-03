import React from 'react'

export default function PurchaseCreate() {
  

  return (
    <div className="border rounded-lg max-w-2xl mx-auto mt-10 pb-3 p-5 space-y-5 ">
      <div>
        <div>공동 배달 게시글 제목</div>
        <input type="text" />
      </div>
      <div>
        <div>가게 검색</div>
        <input type="text" />
      </div>
      <div>
        <div>최소금액 (1,000,000원 미만의 금액만 가능)</div>
        <input type="number" />
      </div>
      <div>
        <div>마감기한 (최대 30분까지 가능)</div>
        <input type="number" max="30"/>
      </div>
      <div>
        <div>메뉴선택</div>
        <form action="" value="메뉴선택">
          <select name="" id="" className="border">
            <option value="황금올리브치킨">황금올리브치킨</option>
            <option value="양념치킨">양념치킨</option>
            <option value="자메이카치킨">자메이카치킨</option>
            <option value="황올반+양념반">황올반+양념반</option>
          </select>
        </form>
      </div>
      <div>
        <div>장바구니</div>
        <div className="border rounded-md mx-auto">
          {/* 메뉴 하나하나 */}
          <div className="border-0">
            <div className="w-full flex">
              <div className="w-1/6"><img src="" alt="" />이미지</div>
              <div className="w-4/6">황금올리브치킨</div>
              <div className="w-1/6">20,000원</div>
            </div>
          </div>
          <div className="border-0">
            <div className="w-full flex">
              <div className="w-1/6"><img src="" alt="" />이미지</div>
              <div className="w-4/6">황금올리브치킨</div>
              <div className="w-1/6">20,000원</div>
            </div>
          </div>
        </div>
      </div>
      <div>
        <button type="button" className="main1-full-button w-20">작성완료</button>
      </div>
    </div>
  )
}

