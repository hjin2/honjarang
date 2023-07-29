// ImageInput.js
import React, { useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { imageUpload } from '../../redux/slice/UploadSlice';

const ImageInput = () => {
  const fileInput = useRef(null);
  const dispatch = useDispatch();
  const image = useSelector((state) => state.upload.image)
  const onChange = (e) => {
    if (e.target.files[0]) {
      const selectedFile = e.target.files[0];
      dispatch(imageUpload(selectedFile));

      const reader = new FileReader();
      reader.onload = () => {
        if (reader.readyState === 2) {
          dispatch(imageUpload(reader.result));
        }
      };
      reader.readAsDataURL(selectedFile);
    } else {
      dispatch(
        imageUpload(
          'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png'
        )
      );
    }
  };

  return (
    <>
      <img
        className="h-20 w-20 rounded-full mx-auto cursor-pointer"
        src={image}
        alt="Profile"
        onClick={() => {
          fileInput.current.click();
        }}
      />
      <input
        type="file"
        style={{ display: 'none' }}
        accept="image/jpg,image/png,image/jpeg"
        name="profile_img"
        onChange={onChange}
        ref={fileInput}
      />
    </>
  );
};

export default ImageInput;
