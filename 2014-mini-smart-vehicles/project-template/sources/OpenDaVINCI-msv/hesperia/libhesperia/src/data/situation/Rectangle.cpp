/*
 * Copyright (c) Christian Berger and Bernhard Rumpe, Rheinisch-Westfaelische Technische Hochschule Aachen, Germany.
 *
 * The Hesperia Framework.
 */

#include <sstream>

#include "core/base/Hash.h"
#include "core/base/Deserializer.h"
#include "core/base/SerializationFactory.h"
#include "core/base/Serializer.h"
#include "hesperia/data/situation/Rectangle.h"

namespace hesperia {
    namespace data {
        namespace situation {

            using namespace std;
            using namespace core::base;

            Rectangle::Rectangle() :
                    m_height(0),
                    m_color(),
                    m_length(0),
                    m_width(0) {
                Shape::setType(Shape::RECTANGLE);
            }

            Rectangle::Rectangle(const Rectangle &obj) :
                    Shape(obj),
                    m_height(obj.m_height),
                    m_color(obj.m_color),
                    m_length(obj.getLength()),
                    m_width(obj.getWidth()) {}

            Rectangle::~Rectangle() {}

            Rectangle& Rectangle::operator=(const Rectangle &obj) {
                Shape::operator=(obj);
                setHeight(obj.getHeight());
                setColor(obj.getColor());
                setLength(obj.getLength());
                setWidth(obj.getWidth());
                return (*this);
            }

            void Rectangle::accept(SituationVisitor &visitor) {
                visitor.visit(*this);
            }

            double Rectangle::getHeight() const {
                return m_height;
            }

            void Rectangle::setHeight(const double &h) {
                m_height = h;
            }

            const Vertex3& Rectangle::getColor() const {
                return m_color;
            }

            void Rectangle::setColor(const Vertex3 &c) {
                m_color = c;
            }

            double Rectangle::getLength() const {
                return m_length;
            }

            void Rectangle::setLength(const double l) {
                m_length = l;
            }

            double Rectangle::getWidth() const {
                return m_width;
            }

            void Rectangle::setWidth(const double w) {
                m_width = w;
            }

            const string Rectangle::toString() const {
                stringstream s;
                s << Shape::toString() << ", Height: " << m_height << ", Color: " << m_color.toString() << ", length: " << m_length << ", width: " << m_width;
                return s.str();
            }

            ostream& Rectangle::operator<<(ostream &out) const {
                // Serializer super class.
                Shape::operator<<(out);

                SerializationFactory sf;

                Serializer &s = sf.getSerializer(out);

                s.write(CRC32 < OPENDAVINCI_CORE_STRINGLITERAL6('h', 'e', 'i', 'g', 'h', 't') >::RESULT,
                        m_height);

                s.write(CRC32 < OPENDAVINCI_CORE_STRINGLITERAL5('c', 'o', 'l', 'o', 'r') >::RESULT,
                        m_color);

                s.write(CRC32 < OPENDAVINCI_CORE_STRINGLITERAL6('l', 'e', 'n', 'g', 't', 'h') >::RESULT,
                        m_length);

                s.write(CRC32 < OPENDAVINCI_CORE_STRINGLITERAL5('w', 'i', 'd', 't', 'h') >::RESULT,
                        m_width);

                return out;
            }

            istream& Rectangle::operator>>(istream &in) {
                // Deserializer super class.
                Shape::operator>>(in);

                SerializationFactory sf;

                Deserializer &d = sf.getDeserializer(in);

                d.read(CRC32 < OPENDAVINCI_CORE_STRINGLITERAL6('h', 'e', 'i', 'g', 'h', 't') >::RESULT,
                       m_height);

                d.read(CRC32 < OPENDAVINCI_CORE_STRINGLITERAL5('c', 'o', 'l', 'o', 'r') >::RESULT,
                       m_color);

                d.read(CRC32 < OPENDAVINCI_CORE_STRINGLITERAL6('l', 'e', 'n', 'g', 't', 'h') >::RESULT,
                       m_length);

                d.read(CRC32 < OPENDAVINCI_CORE_STRINGLITERAL5('w', 'i', 'd', 't', 'h') >::RESULT,
                       m_width);

                return in;
            }

        }
    }
} // hesperia::data::situation
