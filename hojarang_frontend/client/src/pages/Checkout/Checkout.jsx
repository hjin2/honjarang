import { useEffect, useRef } from 'react';
import { loadPaymentWidget } from '@tosspayments/payment-widget-sdk';
import { v4 as uuidv4 } from 'uuid';
import axios from 'axios';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { useParams } from 'react-router-dom';

export default function Checkout() {
  const {price} = useParams()
  const clientKey = 'test_ck_Z0RnYX2w532vOD1DZgg3NeyqApQE';
  const customerKey = 'YbX2HuSlsC9uVJW6NMRMj';
  const token = localStorage.getItem("access_token")
  const paymentWidgetRef = useRef(null);
  const uuid = uuidv4();
  const nickname = useSelector((state) => state.userinfo.nickname)
  const navigate = useNavigate()
  useEffect(() => {
    (async () => {
      const paymentWidget = await loadPaymentWidget(clientKey, customerKey);

      paymentWidget.renderPaymentMethods('#payment-widget', price);

      paymentWidgetRef.current = paymentWidget;
    })();
  }, []);

  return (
    <div>
      <div id="payment-widget" />
      <button
        className='main1-full-button w-20 m-5'
        onClick={async () => {
          const paymentWidget = paymentWidgetRef.current;

          await paymentWidget
            .requestPayment({
              orderId: uuid,
              orderName : `${price}원`,
              customerName: nickname,
              custoemrEmail : 'cust@naver.com'
            })
            .then(function(data){
              const headers = {
                'Authorization': `Bearer ${token}`
              };
              console.log(data)
              const paymentKey = data.paymentKey
              const orderId = data.orderId
              const amount = data.amount
              const paymenttype = data.paymentType
              
              axios.post(`${import.meta.env.VITE_APP_API}/api/v1/users/success`,
                data = {
                  payment_key : paymentKey,
                  order_id : orderId,
                  amount : amount,
                },
                {headers}
              )
                .then(function(response){
                  console.log(response)
                })
                .catch(function(err){
                  console.log(err)
                })
              const successUrl = `/checkout/success?paymentKey=${paymentKey}&orderId=${orderId}&amount=${amount}&paymentType=${paymenttype}`
              navigate(successUrl, {replace: true})
            })
            .catch(function(err){
              console.log(err)
              const failUrl = `/checkout/fail`
              navigate(failUrl, {replace: true})
            })
        }
      }
      >
        결제하기
      </button>
    </div>
  );
}
