import { useState, useEffect } from 'react';
import { OpenVidu } from 'openvidu-browser';
import axios from 'axios';
import './App.css';
import UserVideoComponent from './UserVideoComponent';

const APPLICATION_SERVER_URL = process.env.NODE_ENV === 'production' ? '' : 'https://demos.openvidu.io/';

function App() {
    const [mySessionId, setMySessionId] = useState('SessionA');
    const [myUserName, setMyUserName] = useState('Participant' + Math.floor(Math.random() * 100));
    const [session, setSession] = useState();
    const [mainStreamManager, setMainStreamManager] = useState();
    const [publisher, setPublisher] = useState();
    const [subscribers, setSubscribers] = useState([]);
    const [currentVideoDevice, setCurrentVideoDevice] = useState();

    useEffect(() => {
        window.addEventListener('beforeunload', onbeforeunload);
        return () => {
            window.removeEventListener('beforeunload', onbeforeunload);
        };
    }, []);

    const onbeforeunload = () => {
        leaveSession();
    };

    const handleChangeSessionId = (e) => {
        setMySessionId(e.target.value);
    };

    const handleChangeUserName = (e) => {
        setMyUserName(e.target.value);
    };

    const handleMainVideoStream = (stream) => {
        if (mainStreamManager !== stream) {
            setMainStreamManager(stream);
        }
    };

    const deleteSubscriber = (streamManager) => {
        setSubscribers((prevSubscribers) => prevSubscribers.filter(sub => sub !== streamManager));
    };

    const joinSession = async () => {
        const OV = new OpenVidu();

        const mySession = OV.initSession();

        mySession.on('streamCreated', (event) => {
            const subscriber = mySession.subscribe(event.stream, undefined);
            setSubscribers((prevSubscribers) => [...prevSubscribers, subscriber]);
        });

        mySession.on('streamDestroyed', (event) => {
            deleteSubscriber(event.stream.streamManager);
        });

        mySession.on('exception', (exception) => {
            console.warn(exception);
        });

        try {
            const token = await getToken();
            mySession.connect(token, { clientData: myUserName });

            const publisher = await OV.initPublisherAsync(undefined, {
                audioSource: undefined,
                videoSource: undefined,
                publishAudio: true,
                publishVideo: true,
                resolution: '640x480',
                frameRate: 30,
                insertMode: 'APPEND',
                mirror: false,
            });

            mySession.publish(publisher);
            const devices = await OV.getDevices();
            const videoDevices = devices.filter(device => device.kind === 'videoinput');
            const currentVideoDeviceId = publisher.stream.getMediaStream().getVideoTracks()[0].getSettings().deviceId;
            const currentVideoDevice = videoDevices.find(device => device.deviceId === currentVideoDeviceId);

            setMainStreamManager(publisher);
            setPublisher(publisher);
            setCurrentVideoDevice(currentVideoDevice);
        } catch (error) {
            console.log('There was an error connecting to the session:', error.code, error.message);
        }

        setSession(mySession);
    };

    const leaveSession = () => {
        if (session) {
            session.disconnect();
        }

        setSession(undefined);
        setSubscribers([]);
        setMySessionId('SessionA');
        setMyUserName('Participant' + Math.floor(Math.random() * 100));
        setMainStreamManager(undefined);
        setPublisher(undefined);
    };

    const switchCamera = async () => {
        try {
            const devices = await OV.getDevices();
            const videoDevices = devices.filter(device => device.kind === 'videoinput');

            if (videoDevices.length > 1) {
                const newVideoDevice = videoDevices.find(device => device.deviceId !== currentVideoDevice.deviceId);

                if (newVideoDevice) {
                    const newPublisher = OV.initPublisher(undefined, {
                        videoSource: newVideoDevice.deviceId,
                        publishAudio: true,
                        publishVideo: true,
                        mirror: true,
                    });

                    await session.unpublish(mainStreamManager);
                    await session.publish(newPublisher);

                    setCurrentVideoDevice(newVideoDevice);
                    setMainStreamManager(newPublisher);
                    setPublisher(newPublisher);
                }
            }
        } catch (e) {
            console.error(e);
        }
    };

    const getToken = async () => {
        const sessionId = await createSession(mySessionId);
        return await createToken(sessionId);
    };

    const createSession = async (sessionId) => {
        const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
            headers: { 'Content-Type': 'application/json', },
        });
        return response.data;
    };

    const createToken = async (sessionId) => {
        const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
            headers: { 'Content-Type': 'application/json', },
        });
        return response.data;
    };

    return (
        <div className="container">
            {session === undefined ? (
                <div id="join">
                    {/* ... (JSX for the join session form) ... */}
                </div>
            ) : null}

            {session !== undefined ? (
                <div id="session">
                    {/* ... (JSX for the ongoing session) ... */}
                </div>
            ) : null}
        </div>
    );
}

export default App;
