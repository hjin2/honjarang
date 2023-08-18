import React from 'react';

const Rooms = ({ roomsData, component: RoomComponent }) => {
  return (
    <div className="grid grid-cols-4 gap-4">
      {roomsData.map((room) => (
        <RoomComponent key={room.id} {...room} />
      ))}
    </div>
  );
};

export default Rooms;
