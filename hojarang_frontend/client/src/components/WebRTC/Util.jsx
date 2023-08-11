import axios from "axios";
const APPLICATION_SERVER_URL = "https://honjarang.kro.kr:8443";



export const createSession = async (sessionId) => {
	const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions', { customSessionId: sessionId }, {
		headers: { 'Content-Type': 'application/json', },
	});
	return response.data; // The sessionId
};

export const createToken = async (sessionId) => {
	const response = await axios.post(APPLICATION_SERVER_URL + 'api/sessions/' + sessionId + '/connections', {}, {
		headers: { 'Content-Type': 'application/json', },
	});
	return response.data; // The token
};

  