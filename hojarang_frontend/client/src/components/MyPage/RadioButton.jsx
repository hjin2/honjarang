import React from 'react'

export default function RadioButton(props) {
  const { tab, index } = props;
  return (
    <div className="flex items-center m">
      <input
        checked={index}
        id={`radio-${index}`}
        type="radio" 
        value={tab.title} 
        name="default-radio" 
        className="w-4 h-4 text-blue-600 bg-gray-100 border-gray-300 focus:ring-blue-500"/>
      <label htmlFor={`radio-${index}`} className="ml-2 text-sm font-medium text-gray-900 dark:text-gray-300">{tab.title}</label>
    </div>
  )
}
