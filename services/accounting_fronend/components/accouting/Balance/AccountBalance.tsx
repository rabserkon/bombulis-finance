import {Segmented} from "antd";
import Statistic from "antd/es/statistic/Statistic";
import Card from "antd/es/card/Card";


function AccountBalance({jwtToken}){

    return(
        <Card title="Баланс" bordered={false} className="custom-card">
            <div style={{width: '100%'}}>
                <Segmented
                    options={['Daily', 'Weekly', 'Monthly', 'Yearly', "All"]}
                    onChange={(value) => {
                        console.log(value); // string
                    }}
                    className="margin-bottom-small"
                />
            </div>
            <div style={{width: '100%'}}>
                <Segmented
                    options={['USD', 'EUR', 'RUB']}
                    onChange={(value) => {
                        console.log(value);
                    }}
                    className="margin-bottom-small"
                />
            </div>

            <Statistic
                title="Текущий баланс"
                value={1128.93}
                precision={2}
                suffix="$"
                className="margin-bottom-small"
            />
            <Statistic
                title="Прибыль"
                value={11.93}
                precision={2}
                suffix="$"
                className="margin-bottom-small"
            />
        </Card>
    )

};

export default AccountBalance;