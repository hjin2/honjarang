import React, { useEffect, useRef } from 'react'
import { loadPaymentWidget} from "@tosspayments/payment-widget-sdk"
import { v4 as uuidv4 } from 'uuid'

export default function Checkout() {
  const clientKey = "test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq"
  const customerKey = "YbX2HuSlsC9uVJW6NMRMj"
  const paymentWidgetRef = useRef(null);
  const price = 1000;
  const uuid = uuidv4()

  useEffect(() => {
    (async () => {
      const paymentWidget = await loadPaymentWidget(clientKey, customerKey);

      paymentWidget.renderPaymentMethods("#payment-widget", price);

      paymentWidgetRef.current = paymentWidget;
    })();
  }, []);

  return (
    <div className="App">
      <h1>주문서</h1>
      <div id="payment-widget" />
      <button
        onClick={async () => {
          const paymentWidget = paymentWidgetRef.current;

          try {
            await paymentWidget?.requestPayment({
              orderId: uuid,
              orderName: "토스 티셔츠 외 2건",
              customerName: "김토스",
              customerEmail: "customer123@gmail.com",
              successUrl: `${window.location.origin}/checkout/success`,
              failUrl: `${window.location.origin}/checkout/fail`,
            });
          } catch (err) {
            console.log(err);
          }
        }}
      >
        결제하기
      </button>;
    </div>
  );
}
