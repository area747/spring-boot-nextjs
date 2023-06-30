import { Client } from '@stomp/stompjs';
import { KeyboardEventHandler, useEffect, useState } from 'react';
import SockJS from 'sockjs-client';

export default function Page() {
    const [client, setClient] = useState<Client>();
    const [clientState, setClientState] = useState<string>('init');
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
            onConnect: (e) => {
                stompClient.subscribe('/subscribe/chat/room/1', (data) => {
                    const content = JSON.parse(data.body);
                    setChatLog((chatLog) => [...chatLog, content]);
                });
                stompClient.publish({
                    destination: '/publish/chat/join',
                    body: JSON.stringify({ chatRoomId: 1, writer: '익명' }),
                });
            },
            onDisconnect: (e) => {
                console.log('disConnected');
            },
            reconnectDelay: 0,
        });
        stompClient.activate();
        setClient(stompClient);
    };
    const closeSocket = () => {
        client?.deactivate();
    };

    useEffect(() => {
        if (client) {
            if (client.connected) {
                setClientState('connected');
            } else {
                setClientState('disconnected');
            }
        } else {
            setClientState('init');
        }
    }, [client?.connected, client?.state]);

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
                {
                    {
                        connected: <div>you're in connection</div>,
                        disconnected: (
                            <div>
                                <div>you're disconnected</div>
                                <button onClick={openSocket}>reopenSocket</button>
                            </div>
                        ),
                        init: (
                            <div>
                                <button onClick={openSocket}>openSocket</button>
                            </div>
                        ),
                    }[clientState]
                }
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
