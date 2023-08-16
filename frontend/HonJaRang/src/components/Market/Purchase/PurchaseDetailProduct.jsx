import React from 'react'

export default function PurchaseDetailProduct({content, image})  {
  return (
    <div className="w-full flex flex-col items-center w-3/6">
      <img src={image} alt="상품이미지"/>
      <div className="whitespace-pre-line">
        {content}  
      </div>
    </div>
  );
};
