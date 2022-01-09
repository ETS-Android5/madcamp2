from typing import final
from django.http.response import HttpResponse, JsonResponse
from rest_framework import viewsets
from .serializer import AreaSerializer
from .models import Area
from itertools import chain

class AreaViewSet(viewsets.ModelViewSet):
    queryset = Area.objects.all()
    serializer_class = AreaSerializer

def get_area_by_senses(request):
    print(type(request))
    if request.method == 'POST':
        sight = request.POST.get('sight')
        touch = request.POST.get('touch')
        taste = request.POST.get('taste')

        sightResult = Area.objects.filter(sight = sight)
        touchResult = Area.objects.filter(touch = touch)
        tasteResult = Area.objects.filter(taste = taste)

        sightTouch = sightResult.filter(touch = touch)
        touchTaste = touchResult.filter(taste = taste)
        tasteSight = tasteResult.filter(sight = sight)

        finalResult = sightTouch.filter(taste = taste)
        if(finalResult.count() <= 10) :
            finalResult = sightTouch | touchTaste | tasteSight
            if(finalResult.count() <= 10):
                finalResult = sightResult | touchResult | tasteResult

        finalResult = list(finalResult.values())        

        return JsonResponse(finalResult, safe=False)
        #return HttpResponse(finalResult, content_type="application/json")
        #return HttpResponse(finalResult., content_type="application/json")