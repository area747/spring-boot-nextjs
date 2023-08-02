import { GetServerSideProps } from 'next';
import { DragEvent, DragEventHandler, MouseEvent, MouseEventHandler, useEffect, useState } from 'react';

export default function Page({ initialData }) {
    const [fileList, setFileList] = useState<any[]>([]);
    const loadFileList = async () => {
        const res = await (await fetch('http://127.0.0.1:8080/api/data')).json();
        setFileList(res);
    };

    const onDropEvent: DragEventHandler<HTMLDivElement> = async (event: DragEvent) => {
        event.stopPropagation();
        event.preventDefault();

        function readFile(file: File): Promise<ArrayBuffer> {
            return new Promise((resolve, reject) => {
                const fileReader = new FileReader();
                fileReader.readAsArrayBuffer(file);
                fileReader.onload = () => {
                    if (fileReader.result instanceof ArrayBuffer) {
                        resolve(fileReader.result);
                    }
                };
                fileReader.onerror = reject;
            });
        }
        const files = event.dataTransfer.files;
        const items = event.dataTransfer.items;
        const formData = new FormData();
        for (let i = 0; i < files.length; i++) {
            formData.append('files', files[i], files[i].name);
        }

        const request = new XMLHttpRequest();
        request.open('POST', 'http://127.0.0.1:8080/api/data');
        request.send(formData);
        request.onreadystatechange = () => {
            if (request.readyState == XMLHttpRequest.DONE) {
                console.log(request.responseText);
                loadFileList();
            }
        };
    };
    const deleteBtn = async (item) => {
        await fetch('http://127.0.0.1:8080/api/data', {
            method: 'DELETE',
            body: JSON.stringify(item),
            headers: {
                'Content-Type': 'application/json',
            },
        });
        loadFileList();
    };

    useEffect(() => {
        loadFileList();
    }, []);
    return (
        <div>
            Hello, Next.js!
            <div>{initialData}</div>
            <div
                // draggable="true"
                style={{ width: 250, height: 250, border: '1px solid #eee' }}
                onDragOver={onDragOver}
                onDrop={onDropEvent}
                onDrag={onDragEvent}
            ></div>
            <div>
                {fileList.map((item) => {
                    return (
                        <div>
                            <div
                                onClick={() => {
                                    deleteBtn(item);
                                }}
                            >
                                {item.fileName}
                            </div>
                        </div>
                    );
                })}
            </div>
        </div>
    );
}

const onDragEvent: DragEventHandler<HTMLDivElement> = (event: DragEvent) => {
    console.log(event);
};

const onDragOver: DragEventHandler<HTMLDivElement> = (event: DragEvent) => {
    event.preventDefault();
};

export const getServerSideProps: GetServerSideProps = async () => {
    // 서버 측에서 초기 데이터를 가져옴
    let initialData;
    try {
        const response = await fetch('http://127.0.0.1:8080/api');
        initialData = await response.text();
    } catch (error) {
        console.log('error:::', error);
    }

    return {
        props: {
            initialData,
        },
    };
};
