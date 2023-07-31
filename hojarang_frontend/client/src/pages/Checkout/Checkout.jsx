import React, { useEffect, useRef } from 'react';
import { loadPaymentWidget } from '@tosspayments/payment-widget-sdk';
import { v4 as uuidv4 } from 'uuid';
import { useLocation } from 'react-router';

export default function Checkout() {
  const location = useLocation();
  const clientKey = 'test_ck_D5GePWvyJnrK0W0k6q8gLzN97Eoq';
  const customerKey = 'YbX2HuSlsC9uVJW6NMRMj';
  const paymentWidgetRef = useRef(null);
  const { price, customerName } = location.state || {};
  console.log(price.activeRadio);
  const uuid = uuidv4();
  useEffect(() => {
    (async () => {
      const paymentWidget = await loadPaymentWidget(clientKey, customerKey);

      paymentWidget.renderPaymentMethods('#payment-widget', price.activeRadio);

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
              orderName: `${price.activeRadio}`,
              customerName: customerName,
              customerEmail: 'customer123@gmail.com',
              successUrl: `${window.location.origin}/checkout/success`,
              failUrl: `${window.location.origin}/checkout/fail`,
            });
          } catch (err) {
            console.log(err);
          }
        }}
      >
        결제하기
      </button>
      ;
    </div>
  );
}
