import { GetServerSideProps } from 'next';
import { DragEvent, DragEventHandler, MouseEvent, MouseEventHandler, useEffect, useState } from 'react';

export default function Page({ initialData }) {
    const [isDragging, setIsDragging] = useState<boolean>(false);
    const [fileList, setFileList] = useState<any[]>([]);
    const loadFileList = async () => {
        const res = await (await fetch('http://127.0.0.1:8080/api/data')).json();
        setFileList(res);
    };
    const downloadFile = async (fileId, fileName) => {
        const res = await fetch(`http://127.0.0.1:8080/api/file/${fileId}`);
        const blob = await res.blob();
        const url = URL.createObjectURL(blob);
        const a = document.createElement('a');
        document.body.appendChild(a);
        a.href = url;
        a.download = fileName;
        a.click();
        window.URL.revokeObjectURL(url);
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
        setIsDragging(false);
    };

    const onDragOver: DragEventHandler<HTMLDivElement> = (event: DragEvent) => {
        event.preventDefault();
        setIsDragging(true);
    };

    const onDragLeaveEvent: DragEventHandler<HTMLDivElement> = (event: DragEvent) => {
        event.preventDefault();
        event.stopPropagation();
        setIsDragging(false);
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
        <div style={{ height: '100%' }}>
            <div
                style={{
                    width: '100%',
                    height: 60,
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'end',
                    padding: '10px 10px',
                    boxSizing: 'border-box',
                }}
            >
                <button className="primary-btn">Upload</button>
            </div>
            <div
                style={{
                    width: '100%',
                    height: 1,
                    minHeight: 'calc(100% - 60px)',
                    padding: '10px 10px',
                    boxSizing: 'border-box',
                    margin: 'auto',
                }}
                onDragOver={onDragOver}
                onDrop={onDropEvent}
                onDragLeave={onDragLeaveEvent}
            >
                <div
                    // draggable="true"
                    className={isDragging ? 'drag-over-file' : ''}
                    style={{
                        margin: 'auto',
                        width: '100%',
                        minHeight: '100%',
                        boxSizing: 'border-box',
                        border: '1px solid #eee',
                        display: 'flex',
                        flexWrap: 'wrap',
                        alignContent: 'baseline',
                        padding: 8,
                        gap: 8,
                    }}
                >
                    {fileList.map((item) => {
                        return (
                            <div
                                className="image-container"
                                onClick={() => {
                                    downloadFile(item.id, item.fileName);
                                }}
                            >
                                <div className="image-box">
                                    <div style={{ width: '100%', height: 'calc(100% - 24px)', overflow: 'hidden' }}>
                                        <img
                                            style={{ maxWidth: '100%', height: 'auto' }}
                                            src={`http://127.0.0.1:8080/image/${item.id}.${item.extension}`}
                                        ></img>
                                    </div>
                                    <div
                                        style={{ width: '100%', height: 24, overflow: 'hidden' }}
                                        onClick={() => {
                                            deleteBtn(item);
                                        }}
                                    >
                                        {item.fileName}
                                    </div>
                                </div>
                            </div>
                        );
                    })}
                </div>
            </div>
        </div>
    );
}

export const getServerSideProps: GetServerSideProps = async () => {
    // 서버 측에서 초기 데이터를 가져옴
    const initialData = '';
    // try {
    //     const response = await fetch('http://127.0.0.1:8080/api');
    //     initialData = await response.text();
    // } catch (error) {
    //     console.log('error:::', error);
    // }

    return {
        props: {
            initialData,
        },
    };
};
