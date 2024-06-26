import {List} from "antd";
import {SwapRightOutlined} from "@ant-design/icons";
import Card from "antd/es/card/Card";
import {useEffect, useState} from "react";
import axios from "axios";




function AssetsList({ jwtToken}){
    const [assetsList, setAssetsList] = useState([
        {
            title: 'Loading...',
            key: 'Loading...',
            price: '',
            buyPrice: 'your money'
        }]);
    const [loading, setLoading] = useState(false);


    return (
        <Card title="Активы" className={'custom-card'} bordered={false}>
            <List
                itemLayout="horizontal"
                dataSource={assetsList}
                renderItem={item => (
                    <List.Item  style={{
                        width: '100%'
                    }}>
                        <div style={{
                            display: 'flex',
                            justifyContent: 'space-between',
                            width: '100%'
                        }}>
                            <div>{item.title}</div>
                            <div style={{
                                display: "flex",
                                justifyContent: "flex-end",
                                width: '30%'
                            }}>
                                <div >{item.price}</div>
                                <SwapRightOutlined style={{
                                    marginRight: '5px',
                                    marginLeft: '5px'
                                }}/>
                                <div>{item.buyPrice}</div>
                            </div>
                        </div>
                    </List.Item>

                )}
            />
        </Card>
    );
}

export default AssetsList;