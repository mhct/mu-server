#
# MailFetcher
# 
# @mariohct
# 
# Fetches emails directed to this photo frame
#
import os
import imaplib
import email

user = os.getenv("EMAIL_ACCOUNT")
password = os.getenv("EMAIL_PASSWORD")
photos_dir = os.getenv("PHOTOS_DIR")


##TODO refactor, decouple the acts of FETCH email and PERSIST the images contained on it
def persist_image(photos_path, email_message):
    maintype = email_message.get_content_maintype()
    if maintype == 'multipart':
        for part in email_message.get_payload():
            if part.get_content_maintype() == 'multipart':
                    continue
            if part.get('Content-Disposition') is None:
                    continue

            filename = part.get_filename()
            att_path = os.path.join(photos_path, filename)

            if not os.path.isfile(att_path) :
                    fp = open(att_path, 'wb')
                    fp.write(part.get_payload(decode=True))
                    fp.close()
                    return True

mail = imaplib.IMAP4_SSL('imap.gmail.com')
mail.login(user, password)

mail.list()
mail.select('inbox')

#result, data = mail.search(None, "ALL")
result, data = mail.uid('search', None, '(Subject "mu-photo")')

ids = data[0]
id_list = ids.split()

if  len(id_list) > 0: 
        latest_email_id = id_list[-1]

        print ("latest_email_id", latest_email_id)
        result, data = mail.uid('fetch', latest_email_id, "(RFC822)")
        
        print ("result: ", result)
        #print ("data: ", data)

        raw_email = data[0][1]
        email_instance = email.message_from_string(raw_email)

	if persist_image(photos_dir, email_instance):
                os.putenv("NEW_SLIDESHOW", "1")
        else:
                os.putenv("NEW_SLIDESHOW", "0")

        #print("email dani")

        #print(raw_email)
else:
        print("No email found")

