import React, { useState } from 'react';
import OpenViduVideoComponent from './OvVideo';

export default function UserVideoComponent({ streamManager }) {
    const [showName, setShowName] = useState(false);

    const getNicknameTag = () => {
        // Gets the nickName of the user
        return JSON.parse(streamManager.stream.connection.data).clientData;
    };

    const handleMouseEnter = () => {
        setShowName(true);
    };

    const handleMouseLeave = () => {
        setShowName(false);
    };

    return (
        <div
            className="rounded-2"
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
            style={{ position: 'relative' }}
        >
            <OpenViduVideoComponent streamManager={streamManager} />
            {showName && (
                <div
                    className="nickname-overlay absolute bottom-0 left-0 text-white text-center px-1 w-full bg-gray5 text-sm"
                >
                    {getNicknameTag()}
                </div>
            )}
        </div>
    );
}
