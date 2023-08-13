import axios from "axios";
// const APPLICATION_SERVER_URL = "https://honjarang.kro.kr:8443";
// const APPLICATION_SERVER_URL = "https://demos.openvidu.io";
const APPLICATION_SERVER_URL = "https://honjarang.kro.kr";



export const createSession = async (sessionId) => {
	console.log(sessionId)
	const response = await axios.post(APPLICATION_SERVER_URL + '/api/v1/video-room/sessions', { customSessionId: sessionId }, {
		headers: { 'Content-Type': 'application/json', },
	})
	return response.data; // The sessionId
};

// export const createSession = async (sessionId) => {
// 	console.log(sessionId)
// 	const response = await axios.post(APPLICATION_SERVER_URL + '/api/sessions', { customSessionId: sessionId }, {
// 		headers: { 'Content-Type': 'application/json', },
// 	})
// 	return response.data; // The sessionId
// };

export const createToken = async (sessionId) => {
	const response = await axios.post(APPLICATION_SERVER_URL + '/api/v1/video-room/sessions/' + sessionId + '/connections', {}, {
		headers: { 'Content-Type': 'application/json', },
	});
	console.log(sessionId)
	return response.data; // The token
};
// export const createToken = async (sessionId) => {
// 	const response = await axios.post(APPLICATION_SERVER_URL + '/api/sessions/' + sessionId + '/connections', {}, {
// 		headers: { 'Content-Type': 'application/json', },
// 	});
// 	console.log(sessionId)
// 	return response.data; // The token
// };

  