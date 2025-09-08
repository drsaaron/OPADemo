FROM python

ADD requirements.txt ./requirements.txt
ADD relationshipServer.py ./relationshipServer.py

EXPOSE 25001

RUN pip3 install -r requirements.txt

CMD "./relationshipServer.py"
