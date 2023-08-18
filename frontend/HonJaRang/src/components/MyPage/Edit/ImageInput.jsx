import { useRef, useState } from 'react';
import imageCompression from 'browser-image-compression';
import { v4 as uuidv4 } from 'uuid';
// import AWS from 'aws-sdk';

const ImageInput = ({ imageURL, setImageURL, imageInput, setImageInput }) => {
  const fileInput = useRef(null);
  // const image = useSelector((state) => state.upload.image);
  const [image, setImage] = useState(imageInput);
  const uuid = uuidv4();
  const onChange = async (e) => {
    if (e.target.files[0]) {
      let file = e.target.files[0];
      console.log(file);
      const options = {
        maxSizeMB: 2,
        maxWidthOrHeight: 100,
        fileType: 'image/jpeg',
      };
      try {
        const compressedBlob = await imageCompression(file, options);
        const promise = imageCompression.getDataUrlFromFile(compressedBlob);
        promise.then((result) => {
          const originalExtension = file.name.split('.').pop();
          const compressedFile = new File(
            [compressedBlob],
            `${uuid}.${originalExtension}`,
            {
              type: compressedBlob.type,
            },
          );
          setImageInput(compressedFile);
          setImage(result);
          setImageURL(result);
        });
      } catch (error) {
        console.log(error);
      }
    }
  };
  const removeImage = () => {
    setImage(
      'https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/basic.jpg',
    );
    setImageInput(
      'https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/basic.jpg',
    );
    setImageURL(
      'https://honjarang-bucket.s3.ap-northeast-2.amazonaws.com/profileImage/basic.jpg',
    );
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
      <div className="flex justify-center">
        <button
          type="button"
          className="main5-button w-48"
          onClick={removeImage}
        >
          프로필 사진 삭제하기
        </button>
      </div>
    </>
  );
};

export default ImageInput;
