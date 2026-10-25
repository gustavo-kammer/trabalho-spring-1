import { createContext, useCallback, useContext, useEffect, useId, useRef, useState } from 'react'
import './ConfirmDialog.css'

const ConfirmContext = createContext(null)

// Janela de confirmação do próprio site, no lugar do window.confirm do navegador.
// Uso: const confirm = useConfirm()
//      if (!(await confirm({ title, message, confirmLabel, danger: true }))) return
export function ConfirmProvider({ children }) {
  const [request, setRequest] = useState(null)
  const dialogRef = useRef(null)
  const titleId = useId()
  const messageId = useId()

  const confirm = useCallback(
    (options) => new Promise((resolve) => setRequest({ ...options, resolve })),
    [],
  )

  // <dialog> com showModal: bloqueia a página, prende o foco e fecha com Esc
  useEffect(() => {
    const dialog = dialogRef.current
    if (request && dialog && !dialog.open) dialog.showModal()
  }, [request])

  function close(result) {
    request?.resolve(result)
    dialogRef.current?.close()
    setRequest(null)
  }

  return (
    <ConfirmContext.Provider value={confirm}>
      {children}
      <dialog
        ref={dialogRef}
        className="confirm-dialog"
        aria-labelledby={titleId}
        aria-describedby={messageId}
        onCancel={(event) => {
          event.preventDefault()
          close(false)
        }}
        // clique fora da caixa (no fundo escuro) cancela
        onClick={(event) => {
          if (event.target === event.currentTarget) close(false)
        }}
      >
        {request && (
          <div className="confirm-dialog__body">
            <h2 id={titleId} className="confirm-dialog__title">{request.title}</h2>
            {request.message && (
              <p id={messageId} className="confirm-dialog__message">{request.message}</p>
            )}
            <div className="form__actions">
              {/* Cancelar vem primeiro para receber o foco: Enter sem querer não apaga nada */}
              <button type="button" className="btn" onClick={() => close(false)}>
                {request.cancelLabel ?? 'Cancelar'}
              </button>
              <button
                type="button"
                className={`btn ${request.danger ? 'btn--danger-fill' : 'btn--primary'}`}
                onClick={() => close(true)}
              >
                {request.confirmLabel ?? 'Confirmar'}
              </button>
            </div>
          </div>
        )}
      </dialog>
    </ConfirmContext.Provider>
  )
}

// eslint-disable-next-line react-refresh/only-export-components
export function useConfirm() {
  const confirm = useContext(ConfirmContext)
  if (!confirm) throw new Error('useConfirm precisa estar dentro de <ConfirmProvider>')
  return confirm
}
