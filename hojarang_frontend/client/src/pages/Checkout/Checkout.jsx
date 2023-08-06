import { useEffect, useRef } from 'react';
import { loadPaymentWidget } from '@tosspayments/payment-widget-sdk';
import { v4 as uuidv4 } from 'uuid';
import axios from 'axios';
import { redirect, useLocation } from 'react-router-dom';
import { useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';

export default function Checkout() {
  const clientKey = 'test_ck_Z0RnYX2w532vOD1DZgg3NeyqApQE';
  const customerKey = 'YbX2HuSlsC9uVJW6NMRMj';
  const paymentWidgetRef = useRef(null);
  // const location = useLocation()
  // const price = location.state.price.activeRadio
  const uuid = uuidv4();
  const nickname = useSelector((state) => state.userinfo.nickname)
  const navigate = useNavigate()
  useEffect(() => {
    (async () => {
      const price = window.dataFromOpener
      const paymentWidget = await loadPaymentWidget(clientKey, customerKey);

      paymentWidget.renderPaymentMethods('#payment-widget', price);

      paymentWidgetRef.current = paymentWidget;
    })();
  }, []);

  return (
    <div>
      <h1>주문서</h1>
      <div id="payment-widget" />
      <button
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
                'Authorization': 'Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJpc2NoYXJAbmF2ZXIuY29tIiwicm9sZSI6IlJPTEVfQURNSU4iLCJpYXQiOjE2OTExMzg3MjIsImV4cCI6MTY5MTE0MjMyMn0.N8nkWtk1FpLRfDjNGz6IZ30_m61lhFqhfm-YHMxQvho'
              };
              console.log(data)
              const paymentKey = data.paymentKey
              const orderId = data.orderId
              const amount = data.amount
              const paymenttype = data.paymentType
              
              axios.post('http://honjarang.kro.kr:30000/api/v1/users/success',
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
