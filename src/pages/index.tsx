import { GetServerSideProps } from 'next';
import { DragEvent, DragEventHandler, MouseEvent, MouseEventHandler } from 'react';

export default function Page({ initialData }) {
    return (
        <div>
            Hello, Next.js!
            <div>{initialData}</div>
            <div
                // draggable="true"
                style={{ width: 250, height: 250, border: '1px solid #eee' }}
                onClick={onClickEvent}
                onDragOver={onDragOver}
                onDrop={onDropEvent}
                onDrag={onDragEvent}
            ></div>
        </div>
    );
}

const onDragEvent: DragEventHandler<HTMLDivElement> = (event: DragEvent) => {
    console.log(event);
};

const onDragOver: DragEventHandler<HTMLDivElement> = (event: DragEvent) => {
    event.preventDefault();
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
        }
    };
};

const onClickEvent: MouseEventHandler<HTMLDivElement> = (event: MouseEvent) => {
    console.log(event);
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
