import { useMediaQuery } from 'react-responsive';
import { Mobile, PC, Tablet } from '../component/mediaQuery';
import '../resources/app.css';
import '../resources/button.css';

function MyApp({ Component, pagePros }) {
    return (
        <>
            <Mobile>
                <Component {...pagePros}></Component>
            </Mobile>
            <Tablet>
                <Component {...pagePros}></Component>
            </Tablet>
            <PC>
                <Component {...pagePros}></Component>
            </PC>
        </>
    );
}

export default MyApp;
