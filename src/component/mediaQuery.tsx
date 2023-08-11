import React, { useEffect, useState } from 'react';
import { useMediaQuery } from 'react-responsive';

const Mobile = ({ children }) => {
    const [isMobile, setMobile] = useState(false);
    const mobile = useMediaQuery({
        query: '(max-width:767px)',
    });
    useEffect(() => {
        setMobile(mobile);
    }, [mobile]);
    return <React.Fragment>{isMobile && <div className="mobile-container">{children}</div>}</React.Fragment>;
};

const Tablet = ({ children }) => {
    const [isTablet, setTablet] = useState(false);
    const tablet = useMediaQuery({
        query: '(min-width:768px) and (max-width:1023px)',
    });
    useEffect(() => {
        setTablet(tablet);
    }, [tablet]);
    return <React.Fragment>{isTablet && <div className="tablet-container">{children}</div>}</React.Fragment>;
};

const PC = ({ children }) => {
    const [isPc, setPc] = useState(false);
    const pc = useMediaQuery({
        query: '(min-width:1024px)',
    });
    useEffect(() => {
        setPc(pc);
    }, [pc]);
    return <React.Fragment>{isPc && <div className="pc-container">{children}</div>}</React.Fragment>;
};

export { Mobile, Tablet, PC };
