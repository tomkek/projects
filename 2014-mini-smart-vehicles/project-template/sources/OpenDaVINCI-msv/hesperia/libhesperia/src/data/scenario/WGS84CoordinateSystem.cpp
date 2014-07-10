/*
 * Copyright (c) Christian Berger and Bernhard Rumpe, Rheinisch-Westfaelische Technische Hochschule Aachen, Germany.
 *
 * The Hesperia Framework.
 */

#include "core/base/Hash.h"
#include "core/base/Deserializer.h"
#include "core/base/SerializationFactory.h"
#include "core/base/Serializer.h"
#include "hesperia/data/scenario/WGS84CoordinateSystem.h"

namespace hesperia {
    namespace data {
        namespace scenario {

            using namespace std;
            using namespace core::base;

            WGS84CoordinateSystem::WGS84CoordinateSystem() :
                    m_origin() {}

            WGS84CoordinateSystem::WGS84CoordinateSystem(const WGS84CoordinateSystem &obj) :
                    CoordinateSystem(obj),
                    m_origin(obj.m_origin) {}

            WGS84CoordinateSystem::~WGS84CoordinateSystem() {}

            WGS84CoordinateSystem& WGS84CoordinateSystem::operator=(const WGS84CoordinateSystem &obj) {
                CoordinateSystem::operator=(obj);
                setOrigin(obj.getOrigin());
                return (*this);
            }

            void WGS84CoordinateSystem::accept(ScenarioVisitor &visitor) {
                visitor.visit(*this);

                m_origin.accept(visitor);
            }

            const Vertex3 WGS84CoordinateSystem::getOrigin() const {
                return m_origin;
            }

            void WGS84CoordinateSystem::setOrigin(const Vertex3 &o) {
                m_origin = o;
            }

            const string WGS84CoordinateSystem::toString() const {
                stringstream s;
                s << CoordinateSystem::toString() << ", " << m_origin.toString();
                return s.str();
            }

            ostream& WGS84CoordinateSystem::operator<<(ostream &out) const {
                // Serializer super class.
                CoordinateSystem::operator<<(out);

                SerializationFactory sf;

                Serializer &s = sf.getSerializer(out);

                s.write(CRC32 < OPENDAVINCI_CORE_STRINGLITERAL6('o', 'r', 'i', 'g', 'i', 'n') >::RESULT,
                        m_origin);

                return out;
            }

            istream& WGS84CoordinateSystem::operator>>(istream &in) {
                // Deserializer super class.
                CoordinateSystem::operator>>(in);

                SerializationFactory sf;

                Deserializer &d = sf.getDeserializer(in);

                d.read(CRC32 < OPENDAVINCI_CORE_STRINGLITERAL6('o', 'r', 'i', 'g', 'i', 'n') >::RESULT,
                       m_origin);

                return in;
            }

        }
    }
} // hesperia::data::scenario
