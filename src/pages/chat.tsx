import { Client } from '@stomp/stompjs';
import { KeyboardEventHandler, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';

export default function Page() {
    const [client, setSocket] = useState<Client | null>(null);
    const [message, setMessage] = useState('');
    const [chatLog, setChatLog] = useState<message[]>([]);
    const openSocket = () => {
        const stompClient = new Client({
            brokerURL: 'ws://localhost:8080/socket',
            webSocketFactory: () => new SockJS('http://localhost:8080/socket'),
            connectHeaders: {
                login: 'my-username',
                passcode: 'my-password',
            },
            debug: function (message: string) {
                console.log(message);
            },
            onStompError: function (e) {
                console.log(e);
            },
            reconnectDelay: 5000,
        });
        stompClient.onConnect = (e) => {
            stompClient.subscribe('/subscribe/chat/room/1', (data) => {
                const content = JSON.parse(data.body);
                setChatLog((chatLog) => [...chatLog, content]);
            });
            stompClient.publish({
                destination: '/publish/chat/join',
                body: JSON.stringify({ chatRoomId: 1, writer: '익명' }),
            });
        };
        stompClient.activate();
        setSocket(stompClient);
    };
    const closeSocket = () => {
        client?.deactivate();
    };

    useEffect(() => {
        if (client) {
        }
    }, [client]);

    const sendMessage = () => {
        if (client && message) {
            const messageBody = {
                message,
                writer: '익명',
                chatRoomId: 1,
                messageType: 'JOIN',
            };
            client.publish({
                destination: '/publish/chat/message',
                body: JSON.stringify(messageBody),
            });
            setMessage('');
        }
    };

    return (
        <div>
            <div>
                {chatLog.map((message, index) => (
                    <p key={index}>{`${message.writer} : ${message.message}`}</p>
                ))}
            </div>
            <div>
                {!client ? (
                    <div>
                        <button onClick={openSocket}>openSocket</button>
                    </div>
                ) : (
                    <div>you're in connection</div>
                )}
            </div>
            <input
                type="text"
                value={message}
                onChange={(e) => setMessage(e.target.value)}
                placeholder="Type a message"
            />
            <button onClick={sendMessage}>Send</button>
        </div>
    );
}

type message = {
    chatRoomId: string;
    writer: string;
    message: string;
    type: string;
};
