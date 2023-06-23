import { KeyboardEventHandler, useEffect, useState } from "react";

export default function Page() {
    const [socket, setSocket] = useState<WebSocket|null>(null);
    const [message, setMessage] = useState('');
    const [chatLog, setChatLog] = useState<string[]>([]);
    const openSocket = () => {
        const newSocket = new WebSocket("ws://localhost:8080/socket");
        setSocket(newSocket);
    }
    const closeSocket = () => {
        socket?.close();
    }

    useEffect(() => {
      if (socket) {
        socket.onmessage = (event) => {
            console.log(event);
            const receivedMessage = event.data;
            setChatLog((prevChatLog) => [...prevChatLog, receivedMessage]);
        };
      }
    }, [socket]);

    const sendMessage = () => {
      if (socket && message) {
        socket.send(message);
        setMessage('');
      }
    };

    return (
      <div>
        <div>
          {chatLog.map((message, index) => (
            <p key={index}>{message}</p>
          ))}
        </div>
        <div>
            {!socket?
            <div>
                <button onClick={openSocket}>openSocket</button>
            </div>
            :
            <div>you're in connection</div>}
        </div>
        <input type="text" value={message} onChange={(e) => setMessage(e.target.value)} placeholder="Type a message" />
        <button onClick={sendMessage}>Send</button>
      </div>
    );
}