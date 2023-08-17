import React from 'react'

export default function PurchaseDetailProduct({content, image})  {
  return (
    <div>
      <img src={image} className="w-6/12 mx-auto mt-10" alt="상품이미지"/>
      <div className="whitespace-pre-line mt-5">
        {content}  
      </div>
    </div>
  );
};
