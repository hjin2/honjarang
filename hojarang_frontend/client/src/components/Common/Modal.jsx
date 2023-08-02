export default function Modal({ modalState, setModalState, children }) {
  const onClickCloseButton = () => {
    setModalState(!modalState);
  };
  return (
    <div className="fixed top-0 right-0 bottom-0 left-0 flex items-center justify-center">
      <div
        className="bg-gray5 opacity-70 fixed top-0 right-0 bottom-0 left-0"
        onClick={onClickCloseButton}
      ></div>
      {children}
    </div>
  );
}
