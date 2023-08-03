import React from 'react'


const Rooms = ({roomsData, component: RoomComponent}) => {

  return (
    <div className="flex flex-wrap gap-4 m-5">
      {/* {purchaseData.map((room) => (
        <PurchaseRoom key={room.id} {...room} />
      ))} */}
      {roomsData.map((room) => (
        <RoomComponent key={room.id} {...room} />
      ))}
    </div>
  )
}

export default Rooms;

// const Rooms = ({roomsData, component : Component}) => {
//   function Mycomponent({roomData}){
//     return <div><Component roomData={roomData}/></div>
//   }
//   return (
//     <div className="flex flex-wrap gap-4">
//       {roomsData.map((room) => (
//         <div key={room.id}>
//           <Mycomponent roomData={room}/>
//         </div>
//       ))}
//     </div>
//   )
// }
