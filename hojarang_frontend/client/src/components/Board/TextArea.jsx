import React from 'react'

export const TextArea = ({ label, inputProps, onChange, value }) => {
  return (
      <div className="flex flex-col">
      <label className="mb-2 text-base text-black">{label}</label>
      <textarea
        className="bg-white py-2 px-3 border border-gray3 outline-none resize-none h-64 rounded-lg"
        {...inputProps}
        onChange={onChange}
        value={value}
      ></textarea>
     
      </div>
  );
};

export default TextArea;