// import React from 'react';

export const TextField = ({ label, handleTitle }) => {
  return (
    <div className="flex flex-col">
      <label className="mb-2 text-base text-black">{label}</label>
      <input
        className="bg-white py-2 px-3 border-gray3 outline-none rounded-lg"
        onChange={handleTitle}
      />
    </div>
  );
};

export default TextField;
