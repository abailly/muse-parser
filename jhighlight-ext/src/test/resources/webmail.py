"""Web interface to IMAP account

This package is based on imaplib.py standard library and exports various
classes and functions to browse IMAP accounts through the web.
"""

__version__ = "0.1"

import imaplib

IMAP4_PORT = 143
IMAP4_SSL_PORT = 993

class Account:
    """An account for receiving and sending mails"""
    def __init__(self,login,pwd,host='localhost',port):
        """ Create  a new Account with given parameters
        login: login name of user
        pwd: password for authenticating on server
        host: host name - default to 'localhost'
        port: port number - default to value of IMAP4_PORT
        """

        pass

class IMAPAccount(Account):
    """ An account for connecting to IMAP4 compliant servers"""
    def __init___(self,login,pwd,host,port=IMAP4_PORT):
        super(IMAPAccount,self).__init__(self,login,pwd,host,port)
        # connect to server using IMAP class connection
        self._imap_cnx = IMAP(host,port)
        _imap_cnx.login(user,pwd)
        
        
