import { GetServerSideProps } from "next";

export default function Page({initialData}) {

    return <div>Hello, Next.js!
        <div>
          {initialData}
        </div>
    </div>
}

export const getServerSideProps: GetServerSideProps = async () => {
    // 서버 측에서 초기 데이터를 가져옴
    const response = await fetch('http://localhost:8080/api');
    const initialData = await response.text();

    return {
      props: {
        initialData,
      },
    };
}