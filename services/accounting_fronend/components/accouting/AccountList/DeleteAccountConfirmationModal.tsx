import React from 'react';
import { Modal, Button } from 'antd';

const DeleteConfirmationModal = ({ visible, onCancel, onConfirm, initialValues }) => {
    return (
        <Modal
            title="Подтверждение удаления"
            visible={visible}
            onCancel={onCancel}
            footer={[
                <Button key="cancel" onClick={onCancel}>
                    Отмена
                </Button>,
                <Button key="delete" danger onClick={() => initialValues && onConfirm(initialValues.id)}>
                    Удалить аккаунт
                </Button>,
            ]}
        >
            <p>Вы уверены, что хотите удалить аккаунт?</p>
        </Modal>
    );
};

export default DeleteConfirmationModal;