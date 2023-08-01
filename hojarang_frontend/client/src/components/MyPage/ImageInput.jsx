import { useRef } from 'react';
import { useDispatch, useSelector } from 'react-redux';
import { imageUpload } from '../../redux/slice/UploadSlice';
import axios from 'axios';
// import AWS from 'aws-sdk';


const ImageInput = () => {
  const fileInput = useRef(null);
  const dispatch = useDispatch();
  const image = useSelector((state) => state.upload.image);

  // const REGION = import.meta.env.VITE_APP_REGION;
  // const ACCESS_KEY_ID = import.meta.env.VITE_APP_ACCESS_KEY_ID;
  // const SECRET_ACCESS_KEY = import.meta.env.VITE_APP_SECRET_ACCESS_KEY;

  // AWS.config.update({
  //   region: REGION,
  //   accessKeyId: ACCESS_KEY_ID,
  //   secretAccessKey: SECRET_ACCESS_KEY,
  // });
  const onChange = (e) => {
    if (e.target.files[0]) {
      const selectedFile = e.target.files[0];
      dispatch(imageUpload(selectedFile));
      // const upload = new AWS.S3.ManagedUpload({
      //   params: {
      //     ACL: 'public-read',
      //     Bucket: 'honjarang-bucket',
      //     Key: `upload/${selectedFile.name}`,
      //     Body: selectedFile,
      //   }
      // })
      // upload.promise()
      //   .then(() =>{
      //     console.log('업로드 완료')
      //   })
      //   .catch((err) =>{
      //     console.log(err)
      //   })

      const formData = new FormData();
      formData.append('image', selectedFile);
      axios
        .post('http://localhost:5000/upload', formData, {
          headers: {
            'Content-Type': 'multipart/form-data',
          },
        })
        .then((res) => {
          // console.log(FormData)
          console.log(res);
          alert('성공');
        })
        .catch((err) => {
          console.log(err);
          alert('실패');
        });

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
          'https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png',
        ),
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
