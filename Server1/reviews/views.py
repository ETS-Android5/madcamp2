from json import loads 
from django.db.models.base import Model
from django.http.response import JsonResponse
from rest_framework.viewsets import ModelViewSet
from .models import Review, Area
from .serializer import ReviewSerializer, AreaSerializer
from django.views.decorators.csrf import csrf_exempt
from django.db import connection
import numpy as np

class ReviewViewSet(ModelViewSet):
    queryset = Review.objects.all()
    serializer_class = ReviewSerializer

class AreaViewSet(ModelViewSet):
    queryset = Area.objects.all()
    serializer_class = AreaSerializer

@csrf_exempt
def search_area_by_senses(request):
    if request.method == 'POST':
        dictQuery = loads(request.body.decode('utf-8'))
        
        city = dictQuery['area']['city']
        county = dictQuery['area']['county']
        
        sight = dictQuery['sight']
        touch = dictQuery['touch']
        taste = dictQuery['taste']

        with connection.cursor() as cursor:
            sqlQuery = "CREATE VIEW sightCount AS " + \
                        "SELECT area_id, sight, MAX(cnt) " + \
                        "FROM(SELECT area_id, sight, count(sight) as cnt " + \
                        "FROM reviews_review GROUP BY sight, area_id) GROUP BY area_id;"
            cursor.execute(sqlQuery)
            
            sqlQuery = "CREATE VIEW touchCount AS " + \
                        "SELECT area_id, touch, MAX(cnt) " + \
                        "FROM(SELECT area_id, touch, count(touch) as cnt " + \
                        "FROM reviews_review GROUP BY touch, area_id) GROUP BY area_id;"
            cursor.execute(sqlQuery)

            sqlQuery = "CREATE VIEW tasteCount AS " + \
                        "SELECT area_id, taste, MAX(cnt) " + \
                        "FROM(SELECT area_id, taste, count(taste) as cnt " + \
                        "FROM reviews_review GROUP BY taste, area_id) GROUP BY area_id;"
            cursor.execute(sqlQuery)

            sqlQuery = "SELECT sightCount.area_id, sight, touch, taste " + \
                        "FROM sightCount, touchCount, tasteCount " + \
                        "WHERE sightCount.area_id = touchCount.area_id " + \
                        "AND touchCount.area_id = tasteCount.area_id;"
            cursor.execute(sqlQuery)
            maxSenseofArea = cursor.fetchall()

            sqlQuery = "DROP VIEW sightCount;"
            cursor.execute(sqlQuery)
            sqlQuery = "DROP VIEW touchCount;"
            cursor.execute(sqlQuery)
            sqlQuery = "DROP VIEW tasteCount;"
            cursor.execute(sqlQuery)

            sqlQuery = "SELECT id FROM reviews_area WHERE city = '" + city + "' AND county = '" + county + "';"
            cursor.execute(sqlQuery)
            queryResult = cursor.fetchall()
            findArea = []
            for i in queryResult:
                findArea.append(i[0])

            sightResult = []
            touchResult = []
            tasteResult = []
            sightTouch = []
            touchTaste = []
            tasteSight = []
            allofthem = []

            for i in maxSenseofArea:
                if i[0] in findArea:
                    if sight == i[1]:
                        sightResult.append(i[0])
                        if touch == i[2]:
                            sightTouch.append(i[0])
                            if taste == i[3]:
                                allofthem.append(i[0])
                    if touch == i[2]:
                        touchResult.append(i[0])
                        if taste == i[3]:
                            touchTaste.append(i[0])
                    if taste == i[3]:
                        tasteResult.append(i[0])
                        if sight == i[1]:
                            tasteSight.append(i[0])

            if len(allofthem) < 10 :
                allofthem = np.unique(sightTouch+touchTaste+tasteSight)
                if len(allofthem) < 10:
                    allofthem = np.unique(sightResult + touchResult + tasteResult)
            
            allofthem = allofthem[:10]
            response = Area.objects.filter(id__in=allofthem)
            response = list(response.values())
            response = {"result":response}

        return JsonResponse(response, safe=False)